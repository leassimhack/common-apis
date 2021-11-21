
package mx.parrot.commonapis.controller;

import lombok.extern.slf4j.Slf4j;
import mx.parrot.commonapis.model.User;
import mx.parrot.commonapis.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
public class UserController implements IUserController {

    @Autowired
    private IUserService userService;

    @Override
    public Mono<ResponseEntity<User>> createOrder(String xParrotClientId, String authorization, @Valid User user, String xB3TraceId, String xParrotDevice) {

        long initialMillis = System.currentTimeMillis();

        log.info("Time of response service /: {}", (System.currentTimeMillis() - initialMillis));

        log.info("********** End /debit_credit_instructions_remittances **********");


        return userService.createUser(user)
                .flatMap(user1 -> {
                    log.info("Time of response service /: {}", (System.currentTimeMillis() - initialMillis));

                    log.info("********** End /debit_credit_instructions_remittances **********");
                    return Mono.just(new ResponseEntity<>(user1, HttpStatus.OK));

                });

    }
}


