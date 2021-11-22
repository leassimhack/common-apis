
package mx.parrot.commonapis.controller;

import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RequestMapping("/api/v1")
public interface IOrderController {


    @RequestMapping(value = "/order",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    Mono<ResponseEntity<Void>> createOrder(
            @RequestHeader(value = "X-Parrot-Client-Id", required = false) String xParrotClientId,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            final UriComponentsBuilder builder,
            @RequestHeader(value = "X-B3-TraceId", required = false) String xB3TraceId,
            @RequestHeader(value = "X-User-Id", required = false) Integer userID,
            @RequestHeader(value = "X-Parrot-Device", required = false) String xParrotDevice
    );

    @RequestMapping(value = "/order/{order_id}",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    Mono<ResponseEntity<OrderResponse>> updateOrder(
            @RequestHeader(value = "X-Parrot-Client-Id", required = false) String xParrotClientId,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody OrderRequest orderRequest,
            @RequestHeader(value = "X-B3-TraceId", required = false) String xB3TraceId,
            @RequestHeader(value = "X-User-Id", required = false) Integer userID,
            @RequestHeader(value = "X-Parrot-Device", required = false) String xParrotDevice,
            @PathVariable("order_id") Integer orderId
    );

}


