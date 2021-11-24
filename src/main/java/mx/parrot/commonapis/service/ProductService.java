package mx.parrot.commonapis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.parrot.commonapis.exception.ParrotExceptions;
import mx.parrot.commonapis.model.Amount;
import mx.parrot.commonapis.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_023;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_024;
import static mx.parrot.commonapis.transform.TransformServiceToAPi.transformProductToApi;
import static mx.parrot.commonapis.transform.TransformServiceToDao.transformProduct;
import static mx.parrot.commonapis.transform.TransformServiceToDao.transformProductUpdate;
import static mx.parrot.commonapis.util.ConstantsEnum.CREATED;
import static mx.parrot.commonapis.util.ConstantsEnum.UPDATED;
import static mx.parrot.commonapis.validator.OrderValidation.validateProduct;
import static mx.parrot.commonapis.validator.OrderValidation.validateUpdateProduct;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    @Autowired
    private ProductsHelperService productsHelperService;

    @Autowired
    private OrdersHelperService ordersHelperService;

    @Autowired
    private OrderProductService orderProductService;

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
                                            Integer.compare(t2, t1)
                                    )
                            ).collect(Collectors.toList());

                    return Mono.just(productsFilter);


                });

    }

    @Override
    public Mono<Void> deleteProduct(final String idOrder, final String idProduct) {

        if (!isNumeric(idOrder)) {
            return Mono.error(new ParrotExceptions(PARR_REST_ORD_023.getCode(), PARR_REST_ORD_023.getMessage(), HttpStatus.BAD_REQUEST));
        }

        if (!isNumeric(idProduct)) {
            return Mono.error(new ParrotExceptions(PARR_REST_ORD_024.getCode(), PARR_REST_ORD_024.getMessage(), HttpStatus.BAD_REQUEST));
        }

        final Long longIdOrder = Long.valueOf(idOrder);

        final Long longIdProduct = Long.valueOf(idProduct);


        return ordersHelperService.existsById(longIdOrder.intValue())
                .flatMap(existOrder -> productsHelperService.deleteProduct(longIdProduct.intValue()))
                .doOnSuccess(
                        aVoid -> orderProductService.updateTotalAmount(longIdOrder.intValue())
                                .subscribe(dtoOrderProducts -> {
                                    log.info("*** update total amount***");
                                }));


    }

    @Override
    public Mono<Product> saveProduct(final Product product, final String idOrder) {

        if (!isNumeric(idOrder)) {
            return Mono.error(new ParrotExceptions(PARR_REST_ORD_023.getCode(), PARR_REST_ORD_023.getMessage(), HttpStatus.BAD_REQUEST));
        }

        final Long longIdOrder = Long.valueOf(idOrder);

        return ordersHelperService.existsById(longIdOrder.intValue())
                .flatMap(exist -> validateProduct(product))
                .flatMap(validationOk -> productsHelperService.findAllByNameAndIdOrder(product.getName(), longIdOrder.intValue()))
                .flatMap(validateOk -> productsHelperService.saveProduct(transformProduct(product, CREATED.getValue(), longIdOrder.intValue()))
                        .flatMap(products -> orderProductService.updateTotalAmount(longIdOrder.intValue())
                                .flatMap(dtoOrderProducts -> Mono.just(transformProductToApi(products)))));


    }

    @Override
    public Mono<Product> updateProduct(Product product, String idOrder, String idProduct) {

        if (!isNumeric(idOrder)) {
            return Mono.error(new ParrotExceptions(PARR_REST_ORD_023.getCode(), PARR_REST_ORD_023.getMessage(), HttpStatus.BAD_REQUEST));
        }

        if (!isNumeric(idProduct)) {
            return Mono.error(new ParrotExceptions(PARR_REST_ORD_024.getCode(), PARR_REST_ORD_024.getMessage(), HttpStatus.BAD_REQUEST));
        }

        final Long longIdOrder = Long.valueOf(idOrder);
        final Long longIdProduct = Long.valueOf(idProduct);

        product.setId(longIdProduct.intValue());

        return ordersHelperService.existsById(longIdOrder.intValue())
                .flatMap(exist -> validateUpdateProduct(product))
                .flatMap(existProduct -> productsHelperService.existsById(longIdProduct.intValue()))
                .flatMap(aBoolean -> productsHelperService.getProduct(longIdProduct.intValue()))
                .flatMap(products -> productsHelperService.saveProduct(transformProductUpdate(products, product))
                        .flatMap(productsSave -> orderProductService.updateTotalAmount(longIdOrder.intValue())
                                .flatMap(dtoOrderProducts -> Mono.just(transformProductToApi(productsSave)))));

    }


}
