package mx.parrot.commonapis.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import mx.parrot.commonapis.dao.entity.Orders;
import mx.parrot.commonapis.dao.entity.Products;
import reactor.core.publisher.Flux;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class DtoOrderProducts {
    private Orders orders;
    private Flux<Products> productsFlux;
}
