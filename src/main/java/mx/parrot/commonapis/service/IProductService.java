package mx.parrot.commonapis.service;

import mx.parrot.commonapis.model.Product;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IProductService {

    Mono<List<Product>> getProductReport(final String userID, final String fromDate, final String toDate);

}
