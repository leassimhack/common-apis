package mx.parrot.commonapis.service;


import lombok.extern.slf4j.Slf4j;
import mx.parrot.commonapis.dao.UsersRepository;
import mx.parrot.commonapis.dao.entity.User;
import mx.parrot.commonapis.exception.ParrotExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_000;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_021;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
public class UserHelperService {

    @Autowired
    private UsersRepository repository;

    public Mono<User> getUsers(final String email) {
        return repository.findByEmail(email)
                .doOnNext(p -> log.info("user with id " + p.getId()));
    }

    public Mono<Boolean> existsById(final Integer idUser) {
        return repository.existsById(idUser)
                .flatMap(exist -> {
                    if (exist) {
                        return Mono.just(true);
                    }

                    throw new ParrotExceptions(PARR_REST_ORD_021.getCode(), PARR_REST_ORD_021.getMessage(), BAD_REQUEST);


                })
                .onErrorResume(throwable -> {
                    if (throwable instanceof ParrotExceptions) {

                        ParrotExceptions parrotExceptions = (ParrotExceptions) throwable;
                        throw new ParrotExceptions(parrotExceptions.getCode(), parrotExceptions.getMessage(), parrotExceptions.getHttpStatus());
                    }

                    throw new ParrotExceptions(PARR_REST_ORD_000.getCode(), PARR_REST_ORD_000.getMessage(), INTERNAL_SERVER_ERROR);

                });
    }

    public Mono<User> createUsers(final User user) {
        return this.repository.save(user);
    }


}