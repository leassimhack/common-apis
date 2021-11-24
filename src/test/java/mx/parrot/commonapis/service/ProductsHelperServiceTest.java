package mx.parrot.commonapis.service;

import mx.parrot.commonapis.dao.ProductsRepository;
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

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_004;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_031;
import static mx.parrot.commonapis.factory.CommonApisFactory.getParrotRequest;
import static mx.parrot.commonapis.factory.CommonApisFactory.getProductsDao;
import static mx.parrot.commonapis.util.ConstantsEnum.CREATED;
import static mx.parrot.commonapis.util.ConstantsEnum.UPDATED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
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

        when(productsRepository.findAllByCreatedTimeBetween(anyString(), anyString())).thenReturn(Flux.just(getProductsDao()));

        StepVerifier.create(productsHelperService.getProducts("2021-11-22", "2021-11-22"))
                .assertNext(Assertions::assertNotNull).verifyComplete();

    }

    @Test
    void saveProducts_expectedOk() {

        when(productsRepository.saveAll(anyList())).thenReturn(Flux.just(getProductsDao()));

        StepVerifier.create(productsHelperService.saveAllProducts(1, CREATED.getValue(), getParrotRequest()))
                .assertNext(Assertions::assertNotNull).verifyComplete();

    }


    @Test
    void updateProducts_expectedOk() {

        when(productsRepository.findByIdOrder(any())).thenReturn(Flux.just(getProductsDao()));
        when(productsRepository.saveAll(anyList())).thenReturn(Flux.just(getProductsDao()));


        StepVerifier.create(productsHelperService.saveAllProducts(1, UPDATED.getValue(), getParrotRequest()))
                .assertNext(Assertions::assertNotNull).verifyComplete();

    }

    @Test
    void deleteAllProductsByOrderId_expectedOk() {

        when(productsRepository.findByIdOrder(any())).thenReturn(Flux.just(getProductsDao()));
        when(productsRepository.deleteAll(anyList())).thenReturn(Mono.empty());


        StepVerifier.create(productsHelperService.deleteAllProductsByOrderId(1))
                .verifyComplete();

    }

    @Test
    void deleteProduct_expectedOk() {

        when(productsRepository.existsById(anyInt())).thenReturn(Mono.just(true));
        when(productsRepository.findById(anyInt())).thenReturn(Mono.just(getProductsDao()));
        when(productsRepository.delete(any())).thenReturn(Mono.empty());


        StepVerifier.create(productsHelperService.deleteProduct(1))
                .verifyComplete();

    }

    @Test
    void when_deleteProduct_but_id_not_exist_expectedException() {

        when(productsRepository.existsById(anyInt())).thenReturn(Mono.just(false));

        StepVerifier.create(productsHelperService.deleteProduct(1))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_004.getCode()))
                .verify();

    }

    @Test
    void saveProduct_expectedOk() {

        when(productsRepository.save(any())).thenReturn(Mono.just(getProductsDao()));

        StepVerifier.create(productsHelperService.saveProduct(getProductsDao()))
                .assertNext(Assertions::assertNotNull).verifyComplete();

    }

    @Test
    void findAllByNameAndIdOrder_expectedOk() {

        when(productsRepository.findByIdOrder(any())).thenReturn(Flux.fromIterable(Collections.singletonList(getProductsDao())));

        StepVerifier.create(productsHelperService.findAllByNameAndIdOrder("Water",1))
                .assertNext(Assertions::assertNotNull).verifyComplete();

    }

    @Test
    void when_findAllByNameAndIdOrder_but_exist_name_product_send_parameter_expectedException() {

        when(productsRepository.findByIdOrder(any())).thenReturn(Flux.fromIterable(Collections.singletonList(getProductsDao())));

        StepVerifier.create(productsHelperService.findAllByNameAndIdOrder("Coffe",1))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_ORD_031.getCode()))
                .verify();

    }

}
