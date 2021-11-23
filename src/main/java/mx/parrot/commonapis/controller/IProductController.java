
package mx.parrot.commonapis.controller;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;


@RequestMapping("/api/v1/")
public interface IProductController {

    @RequestMapping(value = "/products/report",
            produces = {MediaType.APPLICATION_PDF_VALUE},
            method = RequestMethod.GET)
    Mono<ResponseEntity<InputStreamResource>> createOrder(
            @RequestHeader(value = "X-Parrot-Client-Id", required = false) String xParrotClientId,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestHeader(value = "X-B3-TraceId", required = false) String xB3TraceId,
            @RequestHeader(value = "X-Parrot-Device", required = false) String xParrotDevice,
            @RequestParam(value = "from_date", required = false) String fromDate,
            @RequestParam(value = "to_date", required = false) String toDate,
            @RequestHeader(value = "X-User-Id", required = false) String userID
    );


}


