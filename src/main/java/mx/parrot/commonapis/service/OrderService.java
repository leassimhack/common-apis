package mx.parrot.commonapis.service;

import lombok.RequiredArgsConstructor;
import mx.parrot.commonapis.dao.entity.Orders;
import mx.parrot.commonapis.dao.entity.Products;
import mx.parrot.commonapis.exception.ParrotExceptions;
import mx.parrot.commonapis.model.DtoOrderProducts;
import mx.parrot.commonapis.model.Order;
import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.OrderResponse;
import mx.parrot.commonapis.model.ParrotRequest;
import mx.parrot.commonapis.transform.TransformServiceToAPi;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_023;
import static mx.parrot.commonapis.transform.TransformServiceToAPi.transformToApi;
import static mx.parrot.commonapis.transform.TransformServiceToDao.transformOrderToEntity;
import static mx.parrot.commonapis.util.ConstantsEnum.CREATED;
import static mx.parrot.commonapis.util.ConstantsEnum.UPDATED;
import static mx.parrot.commonapis.util.Util.getKey;
import static mx.parrot.commonapis.validator.OrderValidation.validateCreateOrder;
import static mx.parrot.commonapis.validator.OrderValidation.validateUpdateOrder;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {


    @Autowired
    private OrdersHelperService ordersService;

    @Autowired
    private ProductsHelperService productsHelperService;

    @Autowired
    private UserHelperService userHelperService;

    @Autowired
    private OrderProductService orderProductService;

    @Override
    public Mono<OrderResponse> updateOrder(final ParrotRequest<OrderRequest> request) {

        return validateUpdateOrder(request)
                .flatMap(validateOk -> userHelperService.existsById(request.getUserId()))
                .flatMap(existUser -> transformOrder(request))
                .flatMap(orders -> ordersService.createOrUpdateOrder(orders))
                .flatMap(orders -> productsHelperService.saveAllProducts(orders.getId(), orders.getStatus(), request)
                        .collectList()
                        .flatMapMany(Flux::fromIterable)
                        .collectList()
                        .flatMap(products -> orderProductService.updateTotalAmount(orders.getId())
                                .flatMap(TransformServiceToAPi::transformToApi)));

    }

    @Override
    public Mono<OrderResponse> getOrder(String idOrder) {

        if (!isNumeric(idOrder)) {
            return Mono.error(new ParrotExceptions(PARR_REST_ORD_023.getCode(), PARR_REST_ORD_023.getMessage(), HttpStatus.BAD_REQUEST));
        }

        final Long id = Long.valueOf(idOrder);

        return ordersService.existsById(id.intValue())
                .flatMap(exist -> ordersService.getOrder(id.intValue()))
                .flatMap(orders -> {
                    Flux<Products> productsFlux = productsHelperService.getProducts(id.intValue());
                    return Mono.just(new DtoOrderProducts().setProductsFlux(productsFlux).setOrders(orders));

                })
                .flatMap(TransformServiceToAPi::transformToApi);
    }

    @Override
    public Mono<Void> deleteOrder(String idOrder) {



        if (!isNumeric(idOrder)) {
            return Mono.error(new ParrotExceptions(PARR_REST_ORD_023.getCode(), PARR_REST_ORD_023.getMessage(), HttpStatus.BAD_REQUEST));
        }

        final Long longIdOrder = Long.valueOf(idOrder);


        return ordersService.existsById(longIdOrder.intValue())
                .flatMap(exist -> productsHelperService.deleteAllProductsByOrderId(longIdOrder.intValue())
                        .flatMapMany(aVoid -> Subscriber::onComplete)
                        .collectList()
                        .flatMap(objects -> ordersService.deleteOrders(longIdOrder.intValue())));


    }


    @Override
    public Mono<Integer> createOrder(final Integer userID, final Map<String, String> headers) {

        return validateCreateOrder(userID, headers)
                .flatMap(validateOk -> userHelperService.existsById(userID))
                .flatMap(validateOk -> {

                    AtomicBoolean exist = new AtomicBoolean(true);
                    Integer idOrder = 0;
                    while (exist.get()) {

                        exist.set(false);
                        idOrder = getKey(userID);

                        ordersService.getOrder(idOrder)
                                .flatMap(orders -> {
                                    exist.set(true);
                                    return null;
                                });

                    }

                    return Mono.just(idOrder);
                });


    }


    private Mono<Orders> transformOrder(final ParrotRequest<OrderRequest> request) {

        final Integer id = Optional.of(request)
                .map(ParrotRequest::getBody)
                .map(OrderRequest::getOrder)
                .map(Order::getId)
                .orElse(null);

        Mono<Orders> order = ordersService.getOrder(id)
                .flatMap(Mono::just)
                .switchIfEmpty(Mono.just(new Orders()));

        return order.flatMap(orders -> {

            final Integer idOrder = Optional.of(orders)
                    .map(Orders::getId).orElse(null);

            final LocalDateTime createdTime = Optional.of(orders)
                    .map(Orders::getCreatedTime).orElse(null);

            if (idOrder != null) {
                return transformOrderToEntity(request, UPDATED.getValue(), createdTime);
            }
            return transformOrderToEntity(request, CREATED.getValue(), createdTime);
        });

    }





}
