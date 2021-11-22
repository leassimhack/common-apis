package mx.parrot.commonapis.service;

import lombok.RequiredArgsConstructor;
import mx.parrot.commonapis.dao.entity.Orders;
import mx.parrot.commonapis.dao.entity.Products;
import mx.parrot.commonapis.model.DtoOrderProducts;
import mx.parrot.commonapis.model.Order;
import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.OrderResponse;
import mx.parrot.commonapis.model.ParrotRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static mx.parrot.commonapis.transform.TransformServiceToAPi.transformToApi;
import static mx.parrot.commonapis.transform.TransformServiceToDao.transformOrderToEntity;
import static mx.parrot.commonapis.util.ConstantsEnum.CREATED;
import static mx.parrot.commonapis.util.ConstantsEnum.UPDATED;
import static mx.parrot.commonapis.util.Util.getKey;
import static mx.parrot.commonapis.validator.OrderValidation.validateUpdateOrder;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {


    @Autowired
    private OrdersHelperService ordersService;

    @Autowired
    private ProductsHelperService productsHelperService;

    @Override
    public Mono<OrderResponse> updateOrder(final ParrotRequest<OrderRequest> request) {

        return validateUpdateOrder(request)
                .flatMap(aBoolean -> transformOrder(request))
                .flatMap(orders -> ordersService.createOrUpdateOrder(orders))
                .flatMap(orders -> {
                    final Flux<Products> dtoOrderProducts = productsHelperService.saveProducts(orders.getId(),orders.getStatus(), request);

                    return Mono.just(new DtoOrderProducts().setProductsFlux(dtoOrderProducts).setOrders(orders));
                })
                .flatMap(dtoOrderProducts -> transformToApi(dtoOrderProducts, request));


    }

    @Override
    public Mono<Integer> createOrder(final Integer userID) {
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
