package mx.parrot.commonapis.service;


import lombok.extern.slf4j.Slf4j;
import mx.parrot.commonapis.dao.UsersRepository;
import mx.parrot.commonapis.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserHelperService {

    @Autowired
    private UsersRepository repository;

    public Mono<User> getUsers(final String email) {
        return repository.findByEmail(email)
                .doOnNext(p -> log.info("user with id " + p.getId()));
    }

    public Mono<User> createUsers(final User user) {
        return this.repository.save(user);
    }


}