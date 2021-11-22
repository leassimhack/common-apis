
package mx.parrot.commonapis.controller;

import lombok.extern.slf4j.Slf4j;
import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.OrderResponse;
import mx.parrot.commonapis.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static mx.parrot.commonapis.transform.TransformApiToService.transformUpdateOrderToService;


@Slf4j
@RestController
public class OrderController implements IOrderController {


    @Autowired
    private IOrderService serviceOrder;


    @Override
    public Mono<ResponseEntity<Void>> createOrder(final String xParrotClientId,
                                                  final String authorization,
                                                  final UriComponentsBuilder builder,
                                                  final String xB3TraceId,
                                                  final Integer userID,
                                                  final String xParrotDevice) {

        long initialMillis = System.currentTimeMillis();

        log.info("Time of response service /: {}", (System.currentTimeMillis() - initialMillis));

        log.info("********** End /api/v1/order **********");

        return serviceOrder.createOrder(userID)
                .flatMap(key -> {

                    log.info("Time of response service /: {}", (System.currentTimeMillis() - initialMillis));
                    log.info("********** End /api/v1/order **********");

                    HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(builder.path("api/v1/order/{id}").buildAndExpand(key).toUri());
                    return Mono.just(new ResponseEntity<>(headers, HttpStatus.CREATED));
                });
    }

    @Override
    public Mono<ResponseEntity<OrderResponse>> updateOrder(final String xParrotClientId,
                                                           final String authorization,
                                                           final @Valid OrderRequest orderRequest,
                                                           final String xB3TraceId,
                                                           final Integer userID,
                                                           final String xParrotDevice,
                                                           final Integer orderId) {

        long initialMillis = System.currentTimeMillis();

        log.info("Time of response service /: {}", (System.currentTimeMillis() - initialMillis));

        log.info("********** End /api/v1/order **********");

        orderRequest.getOrder().setId(orderId);
        return transformUpdateOrderToService(orderRequest, xParrotClientId, userID, xParrotDevice)
                .flatMap(orderRequestParrotRequest -> serviceOrder.updateOrder(orderRequestParrotRequest)
                        .flatMap(orderResponse -> {
                            log.info("Time of response service /: {}", (System.currentTimeMillis() - initialMillis));

                            log.info("********** End /api/v1/order/{order_id} **********");
                            return Mono.just(new ResponseEntity<>(orderResponse, HttpStatus.OK));
                        }));
    }

}



