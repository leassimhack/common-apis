package mx.parrot.commonapis.service;

import mx.parrot.commonapis.exception.ParrotExceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_US_019;
import static mx.parrot.commonapis.factory.CommonApisFactory.getUser;
import static mx.parrot.commonapis.factory.CommonApisFactory.getUserDao;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    private UserHelperService userHelperService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createUser_when_user_dont_exist_expectedOk() {

        when(userHelperService.getUsers(anyString())).thenReturn(Mono.empty());

        when(userHelperService.createUsers(any())).thenReturn(Mono.just(getUserDao()));

        StepVerifier.create(userService.createUser(getUser()))
                .assertNext(Assertions::assertNotNull).verifyComplete();

    }

    @Test
    void createUser_when_email_already_exist_expectedException() {

        when(userHelperService.getUsers(anyString())).thenReturn(Mono.just(getUserDao()));

        StepVerifier.create(userService.createUser(getUser()))
                .expectErrorMatches(throwable -> throwable instanceof ParrotExceptions &&
                        ((ParrotExceptions) throwable).getCode().equals(PARR_REST_US_019.getCode()))
                .verify();

    }

}
