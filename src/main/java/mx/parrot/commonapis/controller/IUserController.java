
package mx.parrot.commonapis.controller;


import mx.parrot.commonapis.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RequestMapping("/api/v1")
public interface IUserController {

    @RequestMapping(value = "/user",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    Mono<ResponseEntity<User>> createOrder(
            @RequestHeader(value = "X-Parrot-Client-Id", required = false) String xParrotClientId,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody User user,
            @RequestHeader(value = "X-B3-TraceId", required = false) String xB3TraceId,
            @RequestHeader(value = "X-Parrot-Device", required = false) String xParrotDevice
    );


}


