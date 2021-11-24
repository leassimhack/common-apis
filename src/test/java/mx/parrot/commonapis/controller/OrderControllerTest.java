package mx.parrot.commonapis.controller;

import mx.parrot.commonapis.model.OrderResponse;
import mx.parrot.commonapis.model.Product;
import mx.parrot.commonapis.service.OrderService;
import mx.parrot.commonapis.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static mx.parrot.commonapis.factory.CommonApisFactory.getOrderRequest;
import static mx.parrot.commonapis.factory.CommonApisFactory.getOrderResponse;
import static mx.parrot.commonapis.factory.CommonApisFactory.getProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OrderControllerTest {


    @InjectMocks
    OrderController orderController;

    @Mock
    OrderService orderService;

    @Mock
    ProductService productService;

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

        assertNotNull(result.getBody());
        assertNotNull(result.getBody().toString());
        assertNotNull(result.getBody().getStatus());
        assertNotNull(result.getBody().getCreate_time());
        assertNotNull(result.getBody().getOrder());
        assertNotNull(result.getBody().getOrder().toString());
        assertNotNull(result.getBody().getOrder().getTotal_amount());
        assertNotNull(result.getBody().getOrder().getTotal_amount().toString());
        assertNotNull(result.getBody().getOrder().getTotal_amount().getValue());
        assertNotNull(result.getBody().getOrder().getTotal_amount().getCurrency());
        assertNotNull(result.getBody().getOrder().getProducts());
        assertNotNull(result.getBody().getOrder().getProducts().toString());
        assertNotNull(result.getBody().getOrder().getProducts().get(0).getAmount().getValue());
        assertNotNull(result.getBody().getOrder().getProducts().get(0).getAmount().getCurrency());
        assertNotNull(result.getBody().getOrder().getProducts().get(0).getQuantity());
        assertNotNull(result.getBody().getOrder().getProducts().get(0).getName());
        assertNotNull(result.getBody().getOrder().getProducts().get(0).getId());
        assertNotNull(result.getBody().getCustomer().toString());
        assertNotNull(result.getBody().getCustomer().getFullName());


    }

    @Test
    void test_createOrder_happy_pad() {

        when(orderService.createOrder(any(), any()))
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

    @Test
    void getOrder_happy_pad() {

        when(orderService.getOrder(anyString()))
                .thenReturn(Mono.just(getOrderResponse()));

        ResponseEntity<?> responseEntity = orderController.getOrder(
                "2321312",
                "55435353",
                "32432432",
                1,
                "web",
                "1"
        ).block();

        assertNotNull(responseEntity.getBody());


    }

    @Test
    void createProduct_happy_pad() {

        Product product =getProduct();

        when(productService.saveProduct(any(),anyString()))
                .thenReturn(Mono.just(product));

        ResponseEntity<Product> responseEntity = orderController.createProduct(
                "2321312",
                "55435353",
                "32432432",
                1,
                "web",
                "1",
                product
        ).block();

        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getName());
        assertNotNull(responseEntity.getBody().getAmount());
        assertNotNull(responseEntity.getBody().getQuantity());
        assertNotNull(responseEntity.getBody().getStatus());
        assertNotNull(responseEntity.getBody().getCreate_time());


    }

    @Test
    void deleteOrder_happy_pad() {

        when(orderService.deleteOrder(anyString())).thenReturn(Mono.empty());

        orderController.deleteOrder(
                "2321312",
                "55435353",
                "32432432",
                1,
                "web",
                "1"
        ).block();

    }

    @Test
    void deleteOrderProduct_happy_pad() {

        when(productService.deleteProduct(anyString(),anyString())).thenReturn(Mono.empty());

        orderController.deleteOrderProduct(
                "2321312",
                "55435353",
                "32432432",
                1,
                "web",
                "1",
                "1"
        ).block();

    }





}
