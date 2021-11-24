package mx.parrot.commonapis.service;


import mx.parrot.commonapis.model.Product;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IProductService {

    Mono<List<Product>> getProductReport(final String userID, final String fromDate, final String toDate);

    Mono<Void> deleteProduct(final String idOrder, final String idProduct);

    Mono<Product> saveProduct(final Product product, final String idOrder);

    Mono<Product> updateProduct(final Product product, final String idOrder, final String idProduct);


}
