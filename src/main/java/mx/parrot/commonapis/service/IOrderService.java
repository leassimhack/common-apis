package mx.parrot.commonapis.service;

import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.OrderResponse;
import mx.parrot.commonapis.model.ParrotRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface IOrderService {

    Mono<Integer> createOrder(final Integer userID, Map<String, String> headers);

    Mono<OrderResponse> updateOrder(final ParrotRequest<OrderRequest> request);

    Mono<OrderResponse> getOrder(final String idOrder);

    Mono<Void> deleteOrder(final String idOrder);

}
