package mx.parrot.commonapis.service;

import mx.parrot.commonapis.dao.OrdersRepository;
import mx.parrot.commonapis.dao.entity.Orders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static mx.parrot.commonapis.factory.CommonApisFactory.getOrdersDao;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderHelperServiceTest {

    @InjectMocks
    OrdersHelperService ordersHelperService;

    @Mock
    private OrdersRepository ordersRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getUsers_findByEmail_expectedOk() {

        when(ordersRepository.findById(anyInt())).thenReturn(Mono.just(getOrdersDao()));

        StepVerifier.create(ordersHelperService.getOrder(1))
                .assertNext(Assertions::assertNotNull).verifyComplete();

    }

    @Test
    void createOrUpdateOrder_expectedOk() {

        when(ordersRepository.save(any())).thenReturn(Mono.just(getOrdersDao()));

        StepVerifier.create(ordersHelperService.createOrUpdateOrder(getOrdersDao()))
                .assertNext(orders -> {
                            assertNotNull(orders.getId());
                            assertNotNull(orders.getCurrency());
                            assertNotNull(orders.getNameCustomer());
                            assertNotNull(orders.getStatus());
                            assertNotNull(orders.getCreatedTime());
                            assertEquals(10.0d, orders.getTotalAmount());
                            assertEquals(1, orders.getIdUser());
                        }
                ).verifyComplete();
    }

    @Test
    void deleteOrders_expectedOk() {

        when(ordersRepository.findById(anyInt())).thenReturn(Mono.just(getOrdersDao()));
        when(ordersRepository.delete(any(Orders.class))).thenReturn(Mono.empty());

        StepVerifier.create(ordersHelperService.deleteOrders(42))
                .verifyComplete();


        verify(ordersRepository).delete(any(Orders.class));

    }

}
