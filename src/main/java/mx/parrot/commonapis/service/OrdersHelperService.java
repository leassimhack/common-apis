package mx.parrot.commonapis.service;

import lombok.extern.slf4j.Slf4j;
import mx.parrot.commonapis.dao.OrdersRepository;
import mx.parrot.commonapis.dao.entity.Orders;
import mx.parrot.commonapis.exception.ParrotExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_000;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_022;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
public class OrdersHelperService {

    @Autowired
    private OrdersRepository repository;

    public Mono<Orders> getOrder(final Integer idOrder) {
        return this.repository.findById(idOrder)
                .doOnNext(p -> log.info("user with id " + p.getId()));
    }


    public Mono<Orders> createOrUpdateOrder(final Orders orders) {

        return this.repository.save(orders);
    }

    public Mono<Boolean> existsById(final Integer idOrder) {

        return this.repository.existsById(idOrder)
                .flatMap(exist -> {
                    if (exist) {
                        return Mono.just(true);
                    }

                    throw new ParrotExceptions(PARR_REST_ORD_022.getCode(), PARR_REST_ORD_022.getMessage(), NOT_FOUND);


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
    public Mono<Void> deleteOrders(final int id) {
        return this.repository.findById(id)
                .flatMap(this.repository::delete);
    }
}