package mx.parrot.commonapis.service;

import mx.parrot.commonapis.dao.UsersRepository;
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

import static mx.parrot.commonapis.factory.CommonApisFactory.getUserDao;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserHelperServiceTest {

    @InjectMocks
    UserHelperService userHelperService;

    @Mock
    private UsersRepository usersRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getUsers_findByEmail_expectedOk() {

        when(usersRepository.findByEmail(anyString())).thenReturn(Mono.just(getUserDao()));

        StepVerifier.create(userHelperService.getUsers("1"))
                .assertNext(Assertions::assertNotNull).verifyComplete();

    }

    @Test
    void createUsers_expectedOk() {

        when(usersRepository.save(any())).thenReturn(Mono.just(getUserDao()));

        StepVerifier.create(userHelperService.createUsers(getUserDao()))
                .assertNext(Assertions::assertNotNull).verifyComplete();
    }

}
