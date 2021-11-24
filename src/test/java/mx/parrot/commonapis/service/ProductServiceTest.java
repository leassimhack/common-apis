package mx.parrot.commonapis.service;

import mx.parrot.commonapis.dao.entity.Products;
import mx.parrot.commonapis.exception.ParrotExceptions;
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

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_022;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_023;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_024;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_031;
import static mx.parrot.commonapis.factory.CommonApisFactory.getDtoOrderProducts;
import static mx.parrot.commonapis.factory.CommonApisFactory.getProduct;
import static mx.parrot.commonapis.factory.CommonApisFactory.getProductsDao;
import static mx.parrot.commonapis.factory.CommonApisFactory.getProductsList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    private ProductsHelperService productsHelperService;

    @Mock
    private OrdersHelperService ordersHelperService;

    @Mock
    private OrderProductService orderProductService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void getProductReport_when_parameters_isOk() {


        Flux<Products> flux = getProductsList()
                .flatMapMany(Flux::fromIterable);

        when(productsHelperService.getProducts(anyString(), anyString())).thenReturn(flux);


        StepVerifier.create(productService.getProductReport("1", "2021-12-10", "2021-23-11"))
                .assertNext(Assertions::assertNotNull
                ).verifyComplete();

    }


    @Test
    void deleteProduct_when_parameters_is_correct_ExpectedOk() {


        when(ordersHelperService.existsById(any())).thenReturn(Mono.just(true));
        when(productsHelperService.deleteProduct(anyInt())).thenReturn(Mono.empty());
        when(orderProductService.updateTotalAmount(anyInt())).thenReturn(Mono.just(getDtoOrderProducts()));

        StepVerifier.create(productService.deleteProduct("1", "1"))
                .verifyComplete();

    }


    @Test
    void deleteProduct_when_id_order_is_not_numeric_expectedException() {

        StepVerifier.create(productService.deleteProduct("1oo", "1"))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_023.getCode()))
                .verify();

    }

    @Test
    void deleteProduct_when_id_product_is_not_numeric_expectedException() {

        StepVerifier.create(productService.deleteProduct("1", "100o"))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_024.getCode()))
                .verify();

    }

    @Test
    void saveProduct_happyPad() {

        when(ordersHelperService.existsById(any())).thenReturn(Mono.just(true));
        when(productsHelperService.findAllByNameAndIdOrder(any(), any())).thenReturn(Mono.just(true));

        when(productsHelperService.saveProduct(any())).thenReturn(Mono.just(getProductsDao()));
        when(orderProductService.updateTotalAmount(anyInt())).thenReturn(Mono.just(getDtoOrderProducts()));

        StepVerifier.create(productService.saveProduct(getProduct(), "1"))
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();

    }

    @Test
    void saveProduct_when_order_id_not_exist_expectedException() {

        when(ordersHelperService.existsById(anyInt())).thenReturn(Mono.error(new ParrotExceptions(PARR_REST_ORD_022.getCode(), PARR_REST_ORD_022.getMessage(), NOT_FOUND)));


        StepVerifier.create(productService.saveProduct(getProduct(), "1"))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_022.getCode()))
                .verify();


    }

    @Test
    void saveProduct_but_name_product_exist_expectedException() {

        when(ordersHelperService.existsById(any())).thenReturn(Mono.just(true));
        when(productsHelperService.findAllByNameAndIdOrder(any(), any())).thenReturn(Mono.error(new ParrotExceptions(PARR_REST_ORD_031.getCode(), PARR_REST_ORD_031.getMessage(), CONFLICT)));

        StepVerifier.create(productService.saveProduct(getProduct(), "1"))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_031.getCode()))
                .verify();

    }

}