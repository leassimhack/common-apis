package mx.parrot.commonapis.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ModelTest {

    private final PodamFactory factory = new PodamFactoryImpl();

    @Test
    void test_Amount() {

        final Amount amount = factory.manufacturePojo(Amount.class);

        assertNotNull(amount.toString());
        assertNotNull(amount.getValue());
        assertNotNull(amount.getCurrency());

    }

    @Test
    void test_Customer() {

        final Customer customer = factory.manufacturePojo(Customer.class);

        assertNotNull(customer.toString());
        assertNotNull(customer.getFirstName());
        assertNotNull(customer.getLastName());
        assertNotNull(customer.getMaidenName());
        assertNotNull(customer.getMiddleName());

    }

    @Test
    void test_Order() {

        final Order order = factory.manufacturePojo(Order.class);

        assertNotNull(order.toString());
        assertNotNull(order.getTotal_amount());
        assertNotNull(order.getId());
        assertNotNull(order.getProducts());

    }


    @Test
    void test_OrderRequest() {

        final OrderRequest orderRequest = factory.manufacturePojo(OrderRequest.class);

        assertNotNull(orderRequest.toString());
        assertNotNull(orderRequest.getOrder());
        assertNotNull(orderRequest.getCustomer());
        assertNotNull(orderRequest.getIdempotentReference());

    }

    @Test
    void test_OrderResponse() {

        final OrderResponse orderResponse = factory.manufacturePojo(OrderResponse.class);

        assertNotNull(orderResponse.toString());
        assertNotNull(orderResponse.getOrder());
        assertNotNull(orderResponse.getCustomer());
        assertNotNull(orderResponse.getIdempotentReference());

    }

    @Test
    void test_ParrotRequest() {

        ParrotRequest parrotRequest = factory.manufacturePojo( ParrotRequest.class, OrderRequest.class);

        assertNotNull(parrotRequest.toString());
        assertNotNull(parrotRequest.getHeaders());
        assertNotNull(parrotRequest.getUserId());
        assertNotNull(parrotRequest.getParams());
        assertNotNull(parrotRequest.getBuilder());
        assertNotNull(parrotRequest.getBody());

    }

    @Test
    void test_User() {

        final User user = factory.manufacturePojo(User.class);

        assertNotNull(user.toString());
        assertNotNull(user.getEmail());
        assertNotNull(user.getName());
        assertNotNull(user.getCreatedTime());
        assertNotNull(user.getPassword());
        assertNotNull(user.getIdUser());

    }

    @Test
    void test_Product() {

        final Product product = factory.manufacturePojo(Product.class);

        assertNotNull(product.toString());
        assertNotNull(product.getAmount());
        assertNotNull(product.getQuantity());
        assertNotNull(product.getName());
        assertNotNull(product.getId_product());

    }
}
