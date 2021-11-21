package mx.parrot.commonapis.dao.entity;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.Assert.assertNotNull;


@ExtendWith(MockitoExtension.class)
class EntityTest {

    private final PodamFactory factory = new PodamFactoryImpl();


    @Test
    void test_Orders() {

        final Orders orders = factory.manufacturePojo(Orders.class);

        assertNotNull(orders.toString());
        assertNotNull(orders.getId());
        assertNotNull(orders.getCreatedTime());
        assertNotNull(orders.getCurrency());
        assertNotNull(orders.getHashOrder());
        assertNotNull(orders.getNameCustomer());
        assertNotNull(orders.getStatus());
        assertNotNull(orders.getUpdateTime());

    }

    @Test
    void test_User() {

        final User user = factory.manufacturePojo(User.class);

        assertNotNull(user.toString());
        assertNotNull(user.getId());
        assertNotNull(user.getCreatedTime());
        assertNotNull(user.getEmail());
        assertNotNull(user.getPassword());

    }

}