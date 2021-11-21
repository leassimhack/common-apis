package mx.parrot.commonapis.controller;

import mx.parrot.commonapis.model.User;
import mx.parrot.commonapis.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static mx.parrot.commonapis.factory.CommonApisFactory.getUser;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {


    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_createUser_happy_pad() {

        when(userService.createUser(any()))
                .thenReturn(Mono.just(getUser()));

        ResponseEntity<User> result = userController.createOrder(
                "2321312",
                "55435353",
                getUser(),
                "32432432",
                "web"

        ).block();

        assertNotNull(result);
    }


}
