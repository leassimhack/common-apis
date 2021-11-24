package mx.parrot.commonapis.transform;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mx.parrot.commonapis.dao.entity.Orders;
import mx.parrot.commonapis.dao.entity.Products;
import mx.parrot.commonapis.model.Amount;
import mx.parrot.commonapis.model.Customer;
import mx.parrot.commonapis.model.DtoOrderProducts;
import mx.parrot.commonapis.model.Order;
import mx.parrot.commonapis.model.OrderResponse;
import mx.parrot.commonapis.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static mx.parrot.commonapis.util.ConstantsEnum.CREATED;
import static mx.parrot.commonapis.util.ConstantsEnum.UPDATED;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransformServiceToAPi {


    public static Mono<OrderResponse> transformToApi(final DtoOrderProducts dtoOrderProducts) {

        Mono<List<Product>> listMono = dtoOrderProducts.getProductsFlux()
                .collectList()
                .map(productsEntity -> {

                    List<Product> products = new ArrayList<>();

                    Flux.fromIterable(productsEntity)
                            .subscribe(productEntity -> {

                                final Product product = new Product();

                                product.setId(productEntity.getId());
                                product.setName(productEntity.getName());
                                product.setAmount(new Amount()
                                        .setValue(BigDecimal.valueOf(productEntity.getAmount()))
                                        .setCurrency(productEntity.getCurrency()));
                                product.setQuantity(productEntity.getQuantity());

                                products.add(product);

                            });

                    return products;
                });


        return listMono.flatMap(products -> {
            OrderResponse orderResponse = new OrderResponse();

            Orders orders = dtoOrderProducts.getOrders();

            orderResponse.setCreate_time(orders.getCreatedTime());
            orderResponse.setCustomer(new Customer().setFullName(orders.getNameCustomer()));
            orderResponse.setStatus(orders.getStatus());
            orderResponse.setUpdate_time(orders.getUpdateTime());
            orderResponse.setOrder(new Order()
                    .setTotal_amount(new Amount()
                            .setValue(BigDecimal.valueOf(orders.getTotalAmount()))
                            .setCurrency(orders.getCurrency())
                    )
                    .setId(orders.getId())
                    .setProducts(products));

            return Mono.just(orderResponse);

        });

    }

    public static Product transformProductToApi(final Products product) {

        Product p = new Product();

        p.setId(product.getId());
        p.setName(product.getName());
        p.setQuantity(product.getQuantity());
        p.setAmount(new Amount().setValue(BigDecimal.valueOf(product.getAmount()))
                .setCurrency(product.getCurrency())
        );
        p.setCreate_time(product.getCreatedTime());
        p.setStatus(product.getStatus());

        p.setUpdate_time(Optional.of(product)
                .map(Products::getUpdateTime)
                .orElse(null));


        return p;

    }

}
