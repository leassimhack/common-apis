package mx.parrot.commonapis.service;


import mx.parrot.commonapis.model.User;
import reactor.core.publisher.Mono;

public interface IUserService {

    Mono<User> createUser(final User request);

}
