package mx.parrot.commonapis.service;

import mx.parrot.commonapis.dao.OrdersRepository;
import mx.parrot.commonapis.dao.ProductsRepository;
import mx.parrot.commonapis.dao.entity.Orders;
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

import java.util.Collections;

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_022;
import static mx.parrot.commonapis.factory.CommonApisFactory.getOrdersDao;
import static mx.parrot.commonapis.factory.CommonApisFactory.getParrotRequest;
import static mx.parrot.commonapis.factory.CommonApisFactory.getProductsDao;
import static mx.parrot.commonapis.util.ConstantsEnum.CREATED;
import static mx.parrot.commonapis.util.ConstantsEnum.UPDATED;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductsHelperServiceTest {

    @InjectMocks
    ProductsHelperService productsHelperService;

    @Mock
    private ProductsRepository productsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getProducts_expectedOk() {

        when(productsRepository.findByIdOrder(anyInt())).thenReturn(Flux.just(getProductsDao()));

        StepVerifier.create(productsHelperService.getProducts(1))
                .assertNext(Assertions::assertNotNull).verifyComplete();

    }

    @Test
    void getProductsByCreatedTime_expectedOk() {

        when(productsRepository.findAllByCreatedTimeBetween(anyString(),anyString())).thenReturn(Flux.just(getProductsDao()));

        StepVerifier.create(productsHelperService.getProducts("2021-11-22", "2021-11-22"))
                .assertNext(Assertions::assertNotNull).verifyComplete();

    }

    @Test
    void saveProducts_expectedOk() {

        when(productsRepository.saveAll(anyList())).thenReturn(Flux.just(getProductsDao()));

        StepVerifier.create(productsHelperService.saveProducts(1,CREATED.getValue(),getParrotRequest()))
                .assertNext(Assertions::assertNotNull).verifyComplete();

    }


    @Test
    void updateProducts_expectedOk() {

        when(productsRepository.findByIdOrder(any())).thenReturn(Flux.just(getProductsDao()));
        when(productsRepository.saveAll(anyList())).thenReturn(Flux.just(getProductsDao()));


        StepVerifier.create(productsHelperService.saveProducts(1,UPDATED.getValue(),getParrotRequest()))
                .assertNext(Assertions::assertNotNull).verifyComplete();

    }



}
