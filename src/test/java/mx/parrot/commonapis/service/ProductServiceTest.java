package mx.parrot.commonapis.service;

import mx.parrot.commonapis.dao.entity.Products;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import static mx.parrot.commonapis.factory.CommonApisFactory.getProductsList;
import static mx.parrot.commonapis.util.ConstantsEnum.X_PARROT_CLIENT_ID;
import static mx.parrot.commonapis.util.ConstantsEnum.X_PARROT_DEVICE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    private ProductsHelperService productsHelperService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void createOrder_when_id_order_dont_exist_expectedIdOrder() {


        Flux<Products> flux = getProductsList()
                .flatMapMany(Flux::fromIterable);

        when(productsHelperService.getProducts(anyString(),anyString())).thenReturn(flux);


        StepVerifier.create(productService.getProductReport("1","2021-12-10","2021-23-11"))
                .assertNext(Assertions::assertNotNull
                ).verifyComplete();

    }



}