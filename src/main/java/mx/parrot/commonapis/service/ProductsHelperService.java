package mx.parrot.commonapis.service;

import lombok.extern.slf4j.Slf4j;
import mx.parrot.commonapis.dao.OrdersRepository;
import mx.parrot.commonapis.dao.ProductsRepository;
import mx.parrot.commonapis.dao.entity.Orders;
import mx.parrot.commonapis.dao.entity.Products;
import mx.parrot.commonapis.model.Order;
import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.ParrotRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
public class ProductsHelperService {

    @Autowired
    private ProductsRepository repository;

    public Flux<Products> getProducts(final Integer idOrder) {
        return this.repository.findByIdOrder(idOrder)
                .doOnNext(p -> log.info("user with id " + p.getId()));
    }



    public Mono<?> saveProducts(final Orders orders, final ParrotRequest<OrderRequest> request) {

        Optional.of(request)
                .map(ParrotRequest::getBody)
                .map(OrderRequest::getOrder)
                .map(Order::getProducts)
                .filter(products -> !products.isEmpty())
                .orElse(Collections.emptyList())
                .forEach(product -> {

                });



    }

    public Flux<Products> private(final Iterable<Products> products) {

        return this.repository.saveAll(products);
    }

}