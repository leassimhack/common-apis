package mx.parrot.commonapis.service;

import lombok.extern.slf4j.Slf4j;
import mx.parrot.commonapis.dao.ProductsRepository;
import mx.parrot.commonapis.dao.entity.Products;
import mx.parrot.commonapis.model.Order;
import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.ParrotRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static mx.parrot.commonapis.util.ConstantsEnum.UPDATED;

@Slf4j
@Service
public class ProductsHelperService {

    @Autowired
    private ProductsRepository repository;

    public Flux<Products> getProducts(final Integer idOrder) {
        return this.repository.findByIdOrder(idOrder)
                .doOnNext(p -> log.info("user with id " + p.getId()));
    }

    public Flux<Products> saveProducts(final Integer idOrder, String status, final ParrotRequest<OrderRequest> request) {

        List<Products> productsListIterable = createIterableProducts(idOrder, status, request);

        if (status.equals(UPDATED.getValue())) {
            return getProducts(idOrder)
                    .flatMap(product -> productsListIterable
                            .stream()
                            .filter(productsReq -> productsReq.getName().equals(product.getName()))
                            .findFirst()
                            .map(products -> {

                                products.setId(product.getId());

                                return Mono.just(products);


                            })
                            .orElse(Mono.just(new Products())))
                    .collectList()
                    .flatMapMany(products -> this.repository.saveAll(Flux.fromIterable(products)));


        }

        return this.repository.saveAll(Flux.fromIterable(productsListIterable));
    }


    private List<Products> createIterableProducts(final Integer idOrder, String status, final ParrotRequest<OrderRequest> request) {


        final List<Products> productsListIterable = new ArrayList<>();


        Optional.of(request)
                .map(ParrotRequest::getBody)
                .map(OrderRequest::getOrder)
                .map(Order::getProducts)
                .filter(products -> !products.isEmpty())
                .orElse(Collections.emptyList())
                .forEach(productApi -> {

                    boolean exist = false;

                    if (!productsListIterable.isEmpty()) {
                        for (int i = 0; i < productsListIterable.size(); i++) {

                            Products p = productsListIterable.get(i);

                            if (productApi.getName().equals(p.getName())) {
                                p.setQuantity(p.getQuantity() + productApi.getQuantity());
                                exist = true;
                                break;
                            }
                        }
                    }


                    if (!exist) {
                        final Products response = new Products();
                        response.setAmount(Double.parseDouble(productApi.getAmount().getValue().toString()));
                        response.setName(productApi.getName());
                        response.setCurrency(productApi.getAmount().getCurrency());
                        response.setQuantity(productApi.getQuantity());
                        response.setIdOrder(idOrder);
                        response.setCreatedTime(LocalDateTime.now());
                        response.setStatus(status);

                        productsListIterable.add(response);
                    }

                });


        return productsListIterable;

    }


}