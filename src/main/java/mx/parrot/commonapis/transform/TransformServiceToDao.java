package mx.parrot.commonapis.transform;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mx.parrot.commonapis.dao.entity.Orders;
import mx.parrot.commonapis.dao.entity.Products;
import mx.parrot.commonapis.model.Amount;
import mx.parrot.commonapis.model.Customer;
import mx.parrot.commonapis.model.Order;
import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.ParrotRequest;
import mx.parrot.commonapis.model.Product;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static mx.parrot.commonapis.util.ConstantsEnum.CREATED;
import static mx.parrot.commonapis.util.ConstantsEnum.UPDATED;
import static org.apache.commons.lang3.StringUtils.EMPTY;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransformServiceToDao {


    public static Mono<Orders> transformOrderToEntity(final ParrotRequest<OrderRequest> request, final String status, LocalDateTime createdTime) {

        OrderRequest orderRequest = request.getBody();

        final String currency =Optional.of(request)
                .map(ParrotRequest::getBody)
                .map(OrderRequest::getOrder)
                .map(Order::getProducts)
                .filter(products -> !products.isEmpty())
                .orElse(emptyList())
                .stream()
                .findFirst()
                .map(Product::getAmount)
                .map(Amount::getCurrency)
                .orElse(EMPTY);

        final String nameCustomer = Optional.ofNullable(orderRequest)
                .map(OrderRequest::getCustomer)
                .map(Customer::getFullName)
                .orElse(EMPTY);



        final Integer idOrder = Optional.ofNullable(orderRequest)
                .map(OrderRequest::getOrder)
                .map(Order::getId)
                .orElse(null);


        final Orders order = new Orders();
        order.setNameCustomer(nameCustomer);
        order.setTotalAmount(Double.parseDouble(String.valueOf(0)));
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

    public static Products transformProduct(final Product product, final String status, int idOrder) {

        Products p = new Products();
        p.setId(product.getId());
        p.setName(product.getName());
        p.setAmount(Double.parseDouble(product.getAmount().getValue().toString()));
        p.setCurrency(product.getAmount().getCurrency());
        p.setQuantity(product.getQuantity());

        if (status.equals(CREATED.getValue())) {
            p.setStatus(CREATED.getValue());
            p.setCreatedTime(LocalDateTime.now());
        } else {
            p.setStatus(UPDATED.getValue());
            p.setUpdateTime(LocalDateTime.now());
        }
        p.setIdOrder(idOrder);

        return p;

    }


    public static Products transformProductUpdate(Products products, final Product product ) {

        Optional.of(product)
                .map(Product::getAmount)
                .map(Amount::getValue)
                .filter(value -> !StringUtils.isBlank(value.toString()))
                .ifPresent(value -> products.setAmount(Double.parseDouble(value.toString())));


        Optional.of(product)
                .map(Product::getAmount)
                .map(Amount::getCurrency)
                .filter(s -> !StringUtils.isBlank(s))
                .ifPresent(products::setCurrency);

        Optional.of(product)
                .map(Product::getQuantity)
                .filter(quantity-> !StringUtils.isBlank(quantity.toString()))
                .ifPresent(products::setQuantity);


        products.setStatus(UPDATED.getValue());
        product.setUpdate_time(LocalDateTime.now());


        return products;

    }

}
