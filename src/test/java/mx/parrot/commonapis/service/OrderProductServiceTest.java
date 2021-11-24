package mx.parrot.commonapis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static mx.parrot.commonapis.factory.CommonApisFactory.getOrdersDao;
import static mx.parrot.commonapis.factory.CommonApisFactory.getProductsDao;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderProductServiceTest {

    @InjectMocks
    OrderProductService orderProductService;

    @Mock
    private OrdersHelperService ordersHelperService;

    @Mock
    private ProductsHelperService productsHelperService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createOrder_when_id_order_dont_exist_expectedIdOrder() {

        when(ordersHelperService.getOrder(anyInt())).thenReturn(Mono.just(getOrdersDao()));
        when(productsHelperService.getProducts(anyInt())).thenReturn(Flux.fromIterable(Collections.singletonList(getProductsDao())));
        when(ordersHelperService.createOrUpdateOrder(any())).thenReturn(Mono.just(getOrdersDao()));


        StepVerifier.create(orderProductService.updateTotalAmount(1))
                .assertNext(dtoOrderProducts -> {

                    assertNotNull(dtoOrderProducts.getOrders());
                    assertNotNull(dtoOrderProducts.getProductsFlux());

                }).verifyComplete();

    }

}