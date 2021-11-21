package mx.parrot.commonapis.service;

import lombok.RequiredArgsConstructor;
import mx.parrot.commonapis.dao.entity.Orders;
import mx.parrot.commonapis.model.Order;
import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.OrderResponse;
import mx.parrot.commonapis.model.ParrotRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static mx.parrot.commonapis.transform.TransformServiceToDao.transformOrderToService;
import static mx.parrot.commonapis.util.ConstantsEnum.CREATED;
import static mx.parrot.commonapis.util.ConstantsEnum.UPDATED;
import static mx.parrot.commonapis.util.Util.getKey;
import static mx.parrot.commonapis.validator.OrderValidation.validateUpdateOrder;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {


    @Autowired
    private OrdersHelperService ordersService;

    @Override
    public Mono<OrderResponse> updateOrder(final ParrotRequest<OrderRequest> request) {


        return validateUpdateOrder(request)
                .flatMap(aBoolean -> {


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
                            return transformOrderToService(request, UPDATED.getValue(), createdTime);
                        }
                        return transformOrderToService(request, CREATED.getValue(), createdTime);
                    });


                })
                .flatMap(orders -> ordersService.createOrUpdateOrder(orders))
                .flatMap(orders ->  saveProducts(orders,request))
                .flatMap(orders -> Mono.just(new OrderResponse()));


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


}
