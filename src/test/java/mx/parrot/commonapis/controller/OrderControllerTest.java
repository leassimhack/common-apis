package mx.parrot.commonapis.controller;

import mx.parrot.commonapis.model.OrderResponse;
import mx.parrot.commonapis.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static mx.parrot.commonapis.factory.CommonApisFactory.getOrderRequest;
import static mx.parrot.commonapis.factory.CommonApisFactory.getOrderResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OrderControllerTest {


    @InjectMocks
    OrderController orderController;

    @Mock
    OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_updateOrder_happy_pad() {

        when(orderService.updateOrder(any()))
                .thenReturn(Mono.just(getOrderResponse()));

        ResponseEntity<OrderResponse> result = orderController.updateOrder(
                "2321312",
                "55435353",
                getOrderRequest(),
                "32432432",
                34324324,
                "web",
                1
        ).block();

        assertNotNull(result);
    }

    @Test
    void test_createOrder_happy_pad() {

        when(orderService.createOrder(any()))
                .thenReturn(Mono.just(12345678));

        ResponseEntity<?> responseEntity = orderController.createOrder(
                "2321312",
                "55435353",
                UriComponentsBuilder.fromUriString("http://localhost:8086"),
                "32432432",
                1,
                "web"
        ).block();

        assertEquals("http://localhost:8086/api/v1/order/12345678", responseEntity.getHeaders().getLocation().toString());

    }


}
