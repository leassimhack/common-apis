package mx.parrot.commonapis.transform;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mx.parrot.commonapis.dao.entity.Orders;
import mx.parrot.commonapis.model.Amount;
import mx.parrot.commonapis.model.Customer;
import mx.parrot.commonapis.model.Order;
import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.ParrotRequest;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static mx.parrot.commonapis.util.ConstantsEnum.CREATED;
import static org.apache.commons.lang3.StringUtils.EMPTY;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransformServiceToDao {


    public static Mono<Orders> transformOrderToEntity(final ParrotRequest<OrderRequest> request, final String status, LocalDateTime createdTime) {

        OrderRequest orderRequest = request.getBody();

        final String nameCustomer = Optional.ofNullable(orderRequest)
                .map(OrderRequest::getCustomer)
                .map(customer -> {

                    String firstName = Optional.of(customer)
                            .map(Customer::getFirstName)
                            .orElse(EMPTY);

                    String lastName = Optional.of(customer)
                            .map(Customer::getLastName)
                            .orElse(EMPTY);

                    String middleName = Optional.of(customer)
                            .map(Customer::getMiddleName)
                            .orElse(EMPTY);

                    String maidenName = Optional.of(customer)
                            .map(Customer::getMaidenName)
                            .orElse(EMPTY);

                    String fullName = StringUtils.join(firstName, " ", middleName, " ", lastName, " ", maidenName);

                    return fullName.replace("  ", " ");

                })
                .orElse(EMPTY);

        final BigDecimal totalAmount = Optional.ofNullable(orderRequest)
                .map(OrderRequest::getOrder)
                .map(mx.parrot.commonapis.model.Order::getTotal_amount)
                .map(Amount::getValue)
                .orElse(null);

        final String currency = Optional.ofNullable(orderRequest)
                .map(OrderRequest::getOrder)
                .map(mx.parrot.commonapis.model.Order::getTotal_amount)
                .map(Amount::getCurrency)
                .orElse(EMPTY);


        final Integer idOrder = Optional.ofNullable(orderRequest)
                .map(OrderRequest::getOrder)
                .map(Order::getId)
                .orElse(null);


        final Orders order = new Orders();
        order.setNameCustomer(nameCustomer);
        order.setTotalAmount(Double.parseDouble(String.valueOf(totalAmount)));
        order.setCurrency(currency);
        order.setStatus(status);
        if (CREATED.getValue().equals(status)) {
            order.setCreatedTime(LocalDateTime.now());
        } else {
            order.setUpdateTime(LocalDateTime.now());
            order.setCreatedTime(createdTime);
        }

        order.setIdUser(request.getUserId());
        order.setId(idOrder);

        return Mono.just(order);


    }

}
