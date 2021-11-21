package mx.parrot.commonapis.service;

import lombok.RequiredArgsConstructor;
import mx.parrot.commonapis.exception.ParrotExceptions;
import mx.parrot.commonapis.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_US_019;
import static mx.parrot.commonapis.validator.UserValidation.validateUSer;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    @Autowired
    private UserHelperService repository;

    @Override
    public Mono<User> createUser(User request) {

        return validateUSer(request)
                .flatMap(validateOK ->
                        dontExistUser(request)
                                .defaultIfEmpty(Boolean.TRUE)
                                .flatMap(notExist -> {

                                    final mx.parrot.commonapis.dao.entity.User user = new mx.parrot.commonapis.dao.entity.User();
                                    user.setName(request.getName());
                                    user.setEmail(request.getEmail());
                                    user.setCreatedTime(LocalDateTime.now());

                                    return repository.createUsers(user)
                                            .flatMap(user1 -> Mono.just(new User()
                                                    .setIdUser(user1.getId())
                                                    .setEmail(user1.getEmail())
                                                    .setName(user1.getName())
                                                    .setPassword(user1.getPassword())
                                                    .setCreatedTime(user1.getCreatedTime())));
                                }));

    }

    private Mono<Boolean> dontExistUser(User request) {

        final String email = Optional.ofNullable(request).map(User::getEmail).orElse(EMPTY);

        return repository.getUsers(email)
                .flatMap(user -> {
                    if (user.getId() != null) {
                        return Mono.error(new ParrotExceptions(PARR_REST_US_019.getCode(), PARR_REST_US_019.getMessage(), HttpStatus.CONFLICT));
                    }
                    return Mono.just(true);
                })
                .switchIfEmpty(Mono.empty());


    }
}
