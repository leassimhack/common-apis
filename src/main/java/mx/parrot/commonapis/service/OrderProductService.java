package mx.parrot.commonapis.service;

import lombok.RequiredArgsConstructor;
import mx.parrot.commonapis.dao.entity.Products;
import mx.parrot.commonapis.model.DtoOrderProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static mx.parrot.commonapis.util.ConstantsEnum.UPDATED;

@Service
@RequiredArgsConstructor
public class OrderProductService {

    @Autowired
    private OrdersHelperService ordersService;

    @Autowired
    private ProductsHelperService productsHelperService;


    public Mono<DtoOrderProducts> updateTotalAmount(final Integer idOrder) {


        return productsHelperService.getProducts(idOrder)
                .collectList()
                .flatMapMany(Flux::fromIterable)
                .collectList()
                .flatMap(products -> {

                    BigDecimal totalAmount = getTotal_Amount(products);

                    return ordersService.getOrder(idOrder)
                            .flatMap(orders -> {

                                orders.setStatus(UPDATED.getValue());
                                orders.setUpdateTime(LocalDateTime.now());
                                orders.setTotalAmount(Double.parseDouble(totalAmount.toString()));

                                return ordersService.createOrUpdateOrder(orders)
                                        .flatMap(orders1 -> Mono.just(new DtoOrderProducts()
                                                .setOrders(orders1)
                                                .setProductsFlux(Flux.fromIterable(products))
                                        ));
                            });
                });

    }


    private static BigDecimal getTotal_Amount(List<Products> products) {

        final BigDecimal[] total_amount = {BigDecimal.valueOf(0)};

        products.forEach(productDao -> {

            BigDecimal amountDao = BigDecimal.valueOf(Double.parseDouble(productDao.getQuantity().toString()) * productDao.getAmount());

            total_amount[0] = total_amount[0].add(amountDao);

        });

        return total_amount[0];

    }

}
