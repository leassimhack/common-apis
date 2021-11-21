package mx.parrot.commonapis.dao;

import mx.parrot.commonapis.dao.entity.Orders;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// Registered as a Spring Repository (Component)
// Repository = a mechanism for encapsulating storage, retrieval, and search behavior which emulates a collection of objects
public interface OrdersRepository extends ReactiveCrudRepository<Orders, Integer> {
}
