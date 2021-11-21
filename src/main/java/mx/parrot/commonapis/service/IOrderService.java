package mx.parrot.commonapis.service;

import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.OrderResponse;
import mx.parrot.commonapis.model.ParrotRequest;
import reactor.core.publisher.Mono;

public interface IOrderService {

    Mono<OrderResponse> updateOrder(final ParrotRequest<OrderRequest> request);

    Mono<Integer> createOrder(final Integer userID);

}
