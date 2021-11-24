
package mx.parrot.commonapis.controller;


import lombok.extern.slf4j.Slf4j;
import mx.parrot.commonapis.service.IProductService;
import mx.parrot.commonapis.util.PDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;


@Slf4j
@RestController
public class ProductController implements IProductController {


    @Autowired
    private IProductService productService;

    @Override
    public Mono<ResponseEntity<InputStreamResource>> createReport(final String xParrotClientId,
                                                                  final String authorization,
                                                                  final String xB3TraceId,
                                                                  final String xParrotDevice,
                                                                  final String fromDate,
                                                                  final String toDate,
                                                                  final String userID) {
        return productService.getProductReport(userID, fromDate, toDate)
                .flatMap(products -> {

                    ByteArrayInputStream bis = PDFGenerator.customerPDFReport(products);

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-Disposition", "inline; filename=customers.pdf");

                    return Mono.just(ResponseEntity
                            .ok()
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_PDF)
                            .body(new InputStreamResource(bis)));

                });
    }
}


