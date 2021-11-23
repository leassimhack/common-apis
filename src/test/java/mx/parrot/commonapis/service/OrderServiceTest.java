package mx.parrot.commonapis.service;

import mx.parrot.commonapis.exception.ParrotExceptions;
import mx.parrot.commonapis.model.Amount;
import mx.parrot.commonapis.model.Customer;
import mx.parrot.commonapis.model.Order;
import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.Product;
import org.junit.jupiter.api.Assertions;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_001;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_002;

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_003;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_005;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_006;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_007;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_008;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_009;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_010;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_013;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_014;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_020;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_022;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_023;
import static mx.parrot.commonapis.factory.CommonApisFactory.getOrdersDao;
import static mx.parrot.commonapis.factory.CommonApisFactory.getParrotRequest;
import static mx.parrot.commonapis.factory.CommonApisFactory.getProductsDao;
import static mx.parrot.commonapis.util.ConstantsEnum.X_PARROT_CLIENT_ID;
import static mx.parrot.commonapis.util.ConstantsEnum.X_PARROT_DEVICE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    private OrdersHelperService ordersHelperService;

    @Mock
    private ProductsHelperService productsHelperService;

    @Mock
    private UserHelperService userHelperService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void createOrder_when_id_order_dont_exist_expectedIdOrder() {

        when(ordersHelperService.getOrder(any())).thenReturn(Mono.empty());
        when(userHelperService.existsById(anyInt())).thenReturn(Mono.just(true));

        StepVerifier.create(orderService.createOrder(1, getParrotRequest().getHeaders()))
                .assertNext(Assertions::assertNotNull
                ).verifyComplete();

    }

    @Test
    void getOrder_when_id_order_exist_expectedIdOrder() {

        when(ordersHelperService.existsById(any())).thenReturn(Mono.just(true));
        when(ordersHelperService.getOrder(any())).thenReturn(Mono.just(getOrdersDao()));
        when(productsHelperService.getProducts(any())).thenReturn(Flux.just(getProductsDao()));

        StepVerifier.create(orderService.getOrder("1"))
                .assertNext(Assertions::assertNotNull
                ).verifyComplete();

    }

    @Test
    void getOrder_when_id_order_dont_exist_expectedIdOrder() {

        when(ordersHelperService.existsById(any())).thenReturn(Mono.error(new ParrotExceptions(PARR_REST_ORD_022.getCode(), PARR_REST_ORD_022.getMessage(), NOT_FOUND)));


        StepVerifier.create(orderService.getOrder("1"))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_022.getCode()))
                .verify();

    }

    @Test
    void getOrder_when_id_order_is_not_numeric_expectedIdOrder() {


        StepVerifier.create(orderService.getOrder("fgfdgdf"))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_023.getCode()))
                .verify();

    }



    @Test
    void updateOrder_when_id_order_dont_exist_createToNew() {

        when(ordersHelperService.getOrder(any())).thenReturn(Mono.empty());
        when(ordersHelperService.createOrUpdateOrder(any())).thenReturn(Mono.just(getOrdersDao()));
        when(productsHelperService.saveProducts(any(), any(), any())).thenReturn(Flux.just(getProductsDao()));
        when(userHelperService.existsById(anyInt())).thenReturn(Mono.just(true));

        StepVerifier.create(orderService.updateOrder(getParrotRequest()))
                .assertNext(Assertions::assertNotNull).verifyComplete();

    }

    @Test
    void updateOrder_when_id_order_exist_onlyUpdate_expectedOK() {

        when(userHelperService.existsById(any())).thenReturn(Mono.just(true));
        when(ordersHelperService.getOrder(any())).thenReturn(Mono.just(getOrdersDao()));
        when(ordersHelperService.createOrUpdateOrder(any())).thenReturn(Mono.just(getOrdersDao()));
        when(productsHelperService.saveProducts(any(), any(), any())).thenReturn(Flux.just(getProductsDao()));


        StepVerifier.create(orderService.updateOrder(getParrotRequest()))
                .assertNext(Assertions::assertNotNull).verifyComplete();


    }

    @Test
    void createUser_when_customer_full_name_is_null_expectedException() {

        final Customer customer = getParrotRequest().getBody().getCustomer().setFullName("");
        final OrderRequest req = getParrotRequest().getBody().setCustomer(customer);

        StepVerifier.create(orderService.updateOrder(getParrotRequest().setBody(req)))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_003.getCode()))
                .verify();

    }

    @Test
    void createUser_when_Total_amount_value_is_null_expectedException() {

        final Order order = getParrotRequest().getBody().getOrder().setTotal_amount(new Amount().setCurrency("MXN"));

        final OrderRequest req = getParrotRequest().getBody().setOrder(order);

        StepVerifier.create(orderService.updateOrder(getParrotRequest().setBody(req)))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_005.getCode()))
                .verify();

    }

    @Test
    void createUser_when_Total_amount_currency_is_null_expectedException() {

        final Order order = getParrotRequest().getBody().getOrder().setTotal_amount(new Amount().setValue(BigDecimal.valueOf(1)));

        final OrderRequest req = getParrotRequest().getBody().setOrder(order);

        StepVerifier.create(orderService.updateOrder(getParrotRequest().setBody(req)))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_006.getCode()))
                .verify();

    }

    @Test
    void createUser_when_Total_amount_value_is_minor_or_equal_to_zero_expectedException() {

        final Order order = getParrotRequest().getBody().getOrder().setTotal_amount(new Amount().setCurrency("MXN").setValue(BigDecimal.valueOf(0)));

        final OrderRequest req = getParrotRequest().getBody().setOrder(order);

        StepVerifier.create(orderService.updateOrder(getParrotRequest().setBody(req)))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_013.getCode()))
                .verify();

    }

    @Test
    void createUser_when_product_name_is_null_expectedException() {

        final Product product = getParrotRequest().getBody().getOrder().getProducts().get(0).setName("");

        final Order order = getParrotRequest().getBody().getOrder().setProducts(Collections.singletonList(product));

        final OrderRequest req = getParrotRequest().getBody().setOrder(order);

        StepVerifier.create(orderService.updateOrder(getParrotRequest().setBody(req)))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_007.getCode()))
                .verify();

    }

    @Test
    void createUser_when_product_quantity_is_null_expectedException() {

        final Product product = getParrotRequest().getBody().getOrder().getProducts().get(0).setQuantity(null);

        final Order order = getParrotRequest().getBody().getOrder().setProducts(Collections.singletonList(product));

        final OrderRequest req = getParrotRequest().getBody().setOrder(order);

        StepVerifier.create(orderService.updateOrder(getParrotRequest().setBody(req)))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_008.getCode()))
                .verify();

    }

    @Test
    void createUser_when_product_amount_value_is_null_expectedException() {

        final Product product = getParrotRequest().getBody().getOrder().getProducts().get(0).setAmount(new Amount().setCurrency("MXN"));

        final Order order = getParrotRequest().getBody().getOrder().setProducts(Collections.singletonList(product));

        final OrderRequest req = getParrotRequest().getBody().setOrder(order);

        StepVerifier.create(orderService.updateOrder(getParrotRequest().setBody(req)))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_009.getCode()))
                .verify();

    }

    @Test
    void createUser_when_product_amount_currency_is_null_expectedException() {

        final Product product = getParrotRequest().getBody().getOrder().getProducts().get(0).setAmount(new Amount().setValue(BigDecimal.valueOf(1)));

        final Order order = getParrotRequest().getBody().getOrder().setProducts(Collections.singletonList(product));

        final OrderRequest req = getParrotRequest().getBody().setOrder(order);

        StepVerifier.create(orderService.updateOrder(getParrotRequest().setBody(req)))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_010.getCode()))
                .verify();

    }

    @Test
    void createUser_when_product_amount_is_minor_or_equal_to_zero_expectedException() {

        final Product product = getParrotRequest().getBody().getOrder().getProducts().get(0).setAmount(new Amount().setCurrency("MXN").setValue(BigDecimal.valueOf(0)));

        final Order order = getParrotRequest().getBody().getOrder().setProducts(Collections.singletonList(product));

        final OrderRequest req = getParrotRequest().getBody().setOrder(order);

        StepVerifier.create(orderService.updateOrder(getParrotRequest().setBody(req)))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_014.getCode()))
                .verify();

    }


    @Test
    void createUser_when_header_parrot_client_id_is_null_expectedException() {

        final Map<String, String> headers = getParrotRequest().getHeaders();

        headers.replace(X_PARROT_CLIENT_ID.getValue(), null);

        StepVerifier.create(orderService.updateOrder(getParrotRequest().setHeaders(headers)))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_001.getCode()))
                .verify();

    }

    @Test
    void createUser_when_header_parrot_device_null_expectedException() {

        final Map<String, String> headers = getParrotRequest().getHeaders();

        headers.replace(X_PARROT_DEVICE.getValue(), null);

        StepVerifier.create(orderService.updateOrder(getParrotRequest().setHeaders(headers)))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_002.getCode()))
                .verify();

    }

    @Test
    void createUser_when_header_x_user_id_null_expectedException() {


        StepVerifier.create(orderService.updateOrder(getParrotRequest().setUserId(null)))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_020.getCode()))
                .verify();

    }


}