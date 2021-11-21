package mx.parrot.commonapis.dao;


import mx.parrot.commonapis.dao.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UsersRepository extends ReactiveCrudRepository<User, Integer> {

    //Mono<User> findByEmail(String email);
    // The Query annotation provides an SQL statement corresponding to the method
    @Query("select id, name, email, password, created_time from users c where c.email = :email")
    Mono<User> findByEmail(String email);


}
