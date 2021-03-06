package mx.parrot.commonapis.factory;

import mx.parrot.commonapis.dao.entity.Orders;
import mx.parrot.commonapis.dao.entity.Products;
import mx.parrot.commonapis.model.Amount;
import mx.parrot.commonapis.model.Customer;
import mx.parrot.commonapis.model.DtoOrderProducts;
import mx.parrot.commonapis.model.Order;
import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.OrderResponse;
import mx.parrot.commonapis.model.ParrotRequest;
import mx.parrot.commonapis.model.Product;
import mx.parrot.commonapis.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mx.parrot.commonapis.util.ConstantsEnum.X_PARROT_CLIENT_ID;
import static mx.parrot.commonapis.util.ConstantsEnum.X_PARROT_DEVICE;

public class CommonApisFactory {


    public static OrderRequest getOrderRequest() {

        return new OrderRequest()
                .setCustomer(new Customer()
                        .setFullName("Ivon Lorens")

                )
                .setOrder(new Order()
                        .setTotal_amount(new Amount()
                                .setValue(BigDecimal.valueOf(10.0))
                                .setCurrency("MXN")
                        )
                        .setProducts(Arrays.asList(
                                new Product()
                                        .setAmount(new Amount()
                                                .setValue(BigDecimal.valueOf(1))
                                                .setCurrency("MXN"))
                                        .setName("Coffe")
                                        .setQuantity(1),
                                new Product()
                                        .setAmount(new Amount()
                                                .setValue(BigDecimal.valueOf(1))
                                                .setCurrency("MXN"))
                                        .setName("Coffe")
                                        .setQuantity(2)
                        )));


    }

    public static OrderResponse getOrderResponse() {

        return new OrderResponse()
                .setCustomer(new Customer()
                        .setFullName("Ivon Lorens")

                )
                .setOrder(new Order()
                        .setId(1)
                        .setTotal_amount(new Amount()
                                .setValue(BigDecimal.valueOf(10.0))
                                .setCurrency("MXN")
                        )
                        .setProducts(Arrays.asList(
                                new Product()
                                        .setId(1)
                                        .setAmount(new Amount()
                                                .setValue(BigDecimal.valueOf(1))
                                                .setCurrency("MXN"))
                                        .setName("Coffe")
                                        .setQuantity(1),
                                new Product()
                                        .setId(1)
                                        .setAmount(new Amount()
                                                .setValue(BigDecimal.valueOf(1))
                                                .setCurrency("MXN"))
                                        .setName("Coffe")
                                        .setQuantity(2)
                        ))

                )
                .setCreate_time(LocalDateTime.now())
                .setStatus("CREATED");


    }

    public static ParrotRequest<OrderRequest> getParrotRequest() {

        Map<String, String> headers = new HashMap<>();

        headers.put(X_PARROT_CLIENT_ID.getValue(), "2321312");
        headers.put(X_PARROT_DEVICE.getValue(), "web");

        return new ParrotRequest<OrderRequest>()
                .setHeaders(headers)
                .setBody(getOrderRequest())
                .setUserId(34324324);

    }


    public static User getUser() {
        return new User()
                .setIdUser(1)
                .setEmail("example@gmail.com")
                .setName("Ismael")
                .setCreatedTime(LocalDateTime.now());
    }

    public static mx.parrot.commonapis.dao.entity.User getUserDao() {

        final mx.parrot.commonapis.dao.entity.User user = new mx.parrot.commonapis.dao.entity.User();
        user.setId(1);
        user.setEmail("example@gmail.com");
        user.setName("Ismael");
        user.setCreatedTime(LocalDateTime.now());
        user.setPassword("5435345");

        return user;
    }

    public static mx.parrot.commonapis.dao.entity.Products getProductsDao() {

        final mx.parrot.commonapis.dao.entity.Products products = new mx.parrot.commonapis.dao.entity.Products();
        products.setId(1);
        products.setName("Coffe");
        products.setCreatedTime(LocalDateTime.now());
        products.setQuantity(2);
        products.setCurrency("MXN");
        products.setAmount(2.0d);

        return products;
    }


    public static List<Product> getProductsApi() {


        final mx.parrot.commonapis.model.Product products = new mx.parrot.commonapis.model.Product();
        products.setId(1);
        products.setName("Coffe");
        products.setQuantity(2);

        products.setAmount(new Amount().setCurrency("MXN").setValue(BigDecimal.valueOf(1)));


        return Collections.singletonList(products);
    }

    public static Orders getOrdersDao() {
        Orders orders = new Orders();

        orders.setId(21312312);
        orders.setNameCustomer("Ismael Vazquez");
        orders.setTotalAmount(10.0d);
        orders.setCurrency("MXN");
        orders.setStatus("CREATED");
        orders.setCreatedTime(LocalDateTime.now());
        orders.setIdUser(1);

        return orders;

    }

    public static Mono<List<Products>> getProductsList() {
        List<Products> productsList = new ArrayList<>();

        productsList.add(getProductsDao());
        productsList.add(getProductsDao());

        return Mono.just(productsList);

    }

    public static DtoOrderProducts getDtoOrderProducts() {
        DtoOrderProducts dtoOrderProducts = new DtoOrderProducts();
        dtoOrderProducts.setOrders(getOrdersDao());
        dtoOrderProducts.setProductsFlux(Flux.fromIterable(Collections.singletonList(getProductsDao())));

        return dtoOrderProducts;
    }

    public static Product getProduct() {

        return new Product()
                .setAmount(new Amount().setCurrency("MXN").setValue(BigDecimal.valueOf(1.0)))
                .setQuantity(1)
                .setName("Coffe")
                .setStatus("CREATED")
                .setCreate_time(LocalDateTime.now());
    }


}
