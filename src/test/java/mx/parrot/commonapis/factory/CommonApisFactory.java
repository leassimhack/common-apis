package mx.parrot.commonapis.factory;

import mx.parrot.commonapis.dao.entity.Orders;
import mx.parrot.commonapis.model.Amount;
import mx.parrot.commonapis.model.Customer;
import mx.parrot.commonapis.model.Order;
import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.OrderResponse;
import mx.parrot.commonapis.model.ParrotRequest;
import mx.parrot.commonapis.model.Product;
import mx.parrot.commonapis.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static mx.parrot.commonapis.util.ConstantsEnum.X_PARROT_CLIENT_ID;
import static mx.parrot.commonapis.util.ConstantsEnum.X_PARROT_DEVICE;

public class CommonApisFactory {


    public static OrderRequest getOrderRequest() {

        return new OrderRequest()
                .setIdempotentReference("331232132")
                .setCustomer(new Customer()
                        .setFirstName("Ismael")
                        .setLastName("Vazquez")

                )
                .setOrder(new Order()
                        .setTotal_amount(new Amount()
                                .setValue(BigDecimal.valueOf(10.0))
                                .setCurrency("MXP")
                        )
                        .setProducts(Arrays.asList(
                                new Product()
                                        .setAmount(new Amount()
                                                .setValue(BigDecimal.valueOf(1))
                                                .setCurrency("MXP"))
                                        .setName("Coffe")
                                        .setQuantity(1),
                                new Product()
                                        .setAmount(new Amount()
                                                .setValue(BigDecimal.valueOf(1))
                                                .setCurrency("MXP"))
                                        .setName("Coffe")
                                        .setQuantity(2)
                        )));


    }

    public static OrderResponse getOrderResponse() {

        return new OrderResponse()
                .setCustomer(new Customer()
                        .setFirstName("Ismael")
                        .setLastName("Vazquez")
                        .setMiddleName("Esteban")
                        .setMaidenName("Flores")

                )
                .setOrder(new Order()
                        .setId(1)
                        .setTotal_amount(new Amount()
                                .setValue(BigDecimal.valueOf(10.0))
                                .setCurrency("MXP")
                        )
                        .setProducts(Arrays.asList(
                                new Product()
                                        .setId_product("1")
                                        .setAmount(new Amount()
                                                .setValue(BigDecimal.valueOf(1))
                                                .setCurrency("MXP"))
                                        .setName("Coffe")
                                        .setQuantity(1),
                                new Product()
                                        .setId_product("1")
                                        .setAmount(new Amount()
                                                .setValue(BigDecimal.valueOf(1))
                                                .setCurrency("MXP"))
                                        .setName("Coffe")
                                        .setQuantity(2)
                        ))

                )
                .setCreate_time(LocalDateTime.now())
                .setStatus("CREATED")
                .setIdempotentReference("423423423");


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

    public static Orders getOrdersDao() {
        Orders orders = new Orders();

        orders.setId(21312312);
        orders.setHashOrder("435345345345345");
        orders.setNameCustomer("Ismael Vazquez");
        orders.setTotalAmount(10.0d);
        orders.setCurrency("MXN");
        orders.setStatus("CREATED");
        orders.setCreatedTime(LocalDateTime.now());
        orders.setIdUser(1);

        return orders;

    }


}
