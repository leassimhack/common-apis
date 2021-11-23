package mx.parrot.commonapis.dao;


import mx.parrot.commonapis.dao.entity.Products;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProductsRepository extends ReactiveCrudRepository<Products, Integer> {

    @Query("select * from products c where c.id_order = :id_order")
    Flux<Products> findByIdOrder(Integer idOrder);

    @Query("select * from products  p where date(created_time ) >=   :startDate  && date(created_time )  <= :endDate")
    Flux<Products> findAllByCreatedTimeBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);


}
