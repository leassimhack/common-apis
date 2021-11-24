package mx.parrot.commonapis.controller;

import mx.parrot.commonapis.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static mx.parrot.commonapis.factory.CommonApisFactory.getProductsApi;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProductControllerTest {


    @InjectMocks
    ProductController productController;

    @Mock
    ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_updateOrder_happy_pad() {

        when(productService.getProductReport(any(), any(), any())).thenReturn(Mono.just(getProductsApi()));


        ResponseEntity<InputStreamResource> result = productController.createReport(
                "2321312",
                "55435353",
                "32432432",
                "1312312",
                "2021-11-23",
                "2021-11-23",
                "1"
        ).block();

        assertTrue(result.getStatusCode().is2xxSuccessful());

    }


}
