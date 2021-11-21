package mx.parrot.commonapis.service;

import lombok.extern.slf4j.Slf4j;
import mx.parrot.commonapis.dao.OrdersRepository;
import mx.parrot.commonapis.dao.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

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


    @Transactional
    public Mono<Void> deleteOrders(final int id) {
        return this.repository.findById(id)
                .flatMap(this.repository::delete);
    }
}