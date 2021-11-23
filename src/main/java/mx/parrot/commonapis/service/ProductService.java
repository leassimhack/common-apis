package mx.parrot.commonapis.service;

import lombok.RequiredArgsConstructor;
import mx.parrot.commonapis.model.Amount;
import mx.parrot.commonapis.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    @Autowired
    private ProductsHelperService productsHelperService;

    @Override
    public Mono<List<Product>> getProductReport(final String userID, final String fromDate, final String toDate) {


        return productsHelperService.getProducts(fromDate, toDate)
                .collectList()
                .flatMap(products -> {

                    List<Product> productsApi = new ArrayList<>();

                    products.forEach(productDao -> {

                        AtomicBoolean exist = new AtomicBoolean(false);

                        if (!productsApi.isEmpty()) {

                            productsApi
                                    .stream()
                                    .filter(product -> product.getName().equals(productDao.getName()))
                                    .findFirst()
                                    .ifPresent(product -> {

                                        exist.set(true);

                                        int quantity = product.getQuantity();
                                        BigDecimal amount = product.getAmount().getValue();

                                        BigDecimal amountDao = BigDecimal.valueOf(Double.parseDouble(productDao.getQuantity().toString()) * productDao.getAmount());

                                        product.setQuantity(quantity + productDao.getQuantity());
                                        product.getAmount().setValue(amountDao.add(amount));


                                    });

                        }
                        if (!exist.get()) {

                            productsApi.add(
                                    new Product()
                                            .setId(productDao.getId())
                                            .setName(productDao.getName())
                                            .setQuantity(productDao.getQuantity())
                                            .setAmount(new Amount()
                                                    .setValue(BigDecimal.valueOf(Double.parseDouble(productDao.getQuantity().toString()) * productDao.getAmount()))
                                                    .setCurrency(productDao.getCurrency())
                                            )
                            );
                        }

                    });


                    List<Product> productsFilter = productsApi.stream()
                            .sorted(Comparator.comparing(Product::getQuantity, (t1, t2) ->
                                        Integer.compare(t2,t1)
                                    )
                            ).collect(Collectors.toList());

                    return Mono.just(productsFilter);


                });

    }
}
