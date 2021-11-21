package mx.parrot.commonapis.dao;


import mx.parrot.commonapis.dao.entity.Products;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProductsRepository extends ReactiveCrudRepository<Products, Integer> {

    @Query("select * from users c where c.id_order = :id_order")
    Flux<Products> findByIdOrder(Integer idOrder);


}
