package mx.parrot.commonapis.service;

import lombok.extern.slf4j.Slf4j;
import mx.parrot.commonapis.dao.ProductsRepository;
import mx.parrot.commonapis.dao.entity.Products;
import mx.parrot.commonapis.exception.ParrotExceptions;
import mx.parrot.commonapis.model.Order;
import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.ParrotRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_000;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_004;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_024;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_031;
import static mx.parrot.commonapis.util.ConstantsEnum.UPDATED;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
public class ProductsHelperService {

    @Autowired
    private ProductsRepository repository;

    public Flux<Products> getProducts(final Integer idOrder) {
        return this.repository.findByIdOrder(idOrder)
                .doOnNext(p -> log.info("user with id " + p.getId()));
    }

    public Mono<Products> getProduct(final Integer idOrder) {
        return this.repository.findById(idOrder)
                .doOnNext(p -> log.info("user with id " + p.getId()));
    }

    public Mono<Boolean> findAllByNameAndIdOrder(final String name, final Integer idOrder) {
        return this.repository.findByIdOrder(idOrder)
                .collectList()
                .flatMapMany(Flux::fromIterable)
                .collectList()
                .flatMap(products -> {

                    AtomicBoolean exist = new AtomicBoolean(false);

                    products.stream()
                            .filter(products1 -> products1.getName().equals(name))
                            .findFirst()
                            .ifPresent(products1 -> exist.set(true));

                    if (exist.get()) {
                        return Mono.error(new ParrotExceptions(PARR_REST_ORD_031.getCode(), PARR_REST_ORD_031.getMessage(), CONFLICT));
                    }

                    return Mono.just(true);

                })
                .onErrorResume(throwable -> {
                    if (throwable instanceof ParrotExceptions) {

                        ParrotExceptions parrotExceptions = (ParrotExceptions) throwable;
                        throw new ParrotExceptions(parrotExceptions.getCode(), parrotExceptions.getMessage(), parrotExceptions.getHttpStatus());
                    }

                    throw new ParrotExceptions(PARR_REST_ORD_000.getCode(), PARR_REST_ORD_000.getMessage(), INTERNAL_SERVER_ERROR);

                });

    }


    public Flux<Products> getProducts(final String fromDate, final String toDate) {
        return this.repository.findAllByCreatedTimeBetween(fromDate, toDate)
                .doOnNext(p -> log.info("user with id " + p.getId()));
    }

    public Mono<Products> saveProduct(final Products products) {
        return this.repository.save(products)
                .doOnNext(p -> log.info("user with id " + p.getId()));
    }

    public Mono<Boolean> existsById(final Integer idProduct) {
        return this.repository.existsById(idProduct)
                .flatMap(exist -> {
                    if (exist) {
                        return Mono.just(true);
                    }
                    throw new ParrotExceptions(PARR_REST_ORD_024.getCode(), PARR_REST_ORD_024.getMessage(), NOT_FOUND);
                })
                .onErrorResume(throwable -> {
                    if (throwable instanceof ParrotExceptions) {

                        ParrotExceptions parrotExceptions = (ParrotExceptions) throwable;
                        throw new ParrotExceptions(parrotExceptions.getCode(), parrotExceptions.getMessage(), parrotExceptions.getHttpStatus());
                    }

                    throw new ParrotExceptions(PARR_REST_ORD_000.getCode(), PARR_REST_ORD_000.getMessage(), INTERNAL_SERVER_ERROR);

                });
    }


    @Transactional
    public Mono<Void> deleteProduct(final int id) {
        return this.repository.existsById(id)
                .flatMap(exist -> {
                    if (exist) {
                        return Mono.just(true);
                    }
                    throw new ParrotExceptions(PARR_REST_ORD_004.getCode(), PARR_REST_ORD_004.getMessage(), NOT_FOUND);
                })
                .onErrorResume(throwable -> {
                    if (throwable instanceof ParrotExceptions) {

                        ParrotExceptions parrotExceptions = (ParrotExceptions) throwable;
                        throw new ParrotExceptions(parrotExceptions.getCode(), parrotExceptions.getMessage(), parrotExceptions.getHttpStatus());
                    }

                    throw new ParrotExceptions(PARR_REST_ORD_000.getCode(), PARR_REST_ORD_000.getMessage(), INTERNAL_SERVER_ERROR);

                })
                .flatMap(exist -> this.repository.findById(id))
                .flatMap(this.repository::delete);


    }

    @Transactional
    public Mono<Void> deleteAllProductsByOrderId(final Integer idOrder) {

        return this.repository.findByIdOrder(idOrder)
                .collectList()
                .flatMapMany(Flux::fromIterable)
                .collectList()
                .flatMap(products -> this.repository.deleteAll(products));


    }


    public Flux<Products> saveAllProducts(final Integer idOrder, String status, final ParrotRequest<OrderRequest> request) {

        List<Products> productsListIterable = createIterableProducts(idOrder, status, request);

        if (status.equals(UPDATED.getValue())) {
            return getProducts(idOrder)
                    .collectList()
                    .flatMapMany(products -> {

                        List<Products> filterProducts = new ArrayList<>();

                        productsListIterable.forEach(productReq -> products.stream()
                                .filter(p -> p.getName().equals(productReq.getName()))
                                .findFirst()
                                .ifPresent(products1 -> {

                                    products1.setUpdateTime(LocalDateTime.now());
                                    products1.setQuantity(productReq.getQuantity());
                                    products1.setAmount(productReq.getAmount());
                                    products1.setCurrency(productReq.getCurrency());
                                    products1.setStatus(UPDATED.getValue());

                                    filterProducts.add(products1);


                                }));


                        return this.repository.saveAll(filterProducts);
                    });


        }

        return this.repository.saveAll(productsListIterable);
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