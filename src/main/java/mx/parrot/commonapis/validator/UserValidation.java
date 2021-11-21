package mx.parrot.commonapis.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.parrot.commonapis.exception.ParrotExceptions;
import mx.parrot.commonapis.model.User;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_US_017;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_US_018;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserValidation {

    public static Mono<Boolean> validateUSer(final User user) {

        try {

            Optional.of(user)
                    .map(User::getName)
                    .filter(s -> !StringUtils.isBlank(s))
                    .orElseThrow(() -> new ParrotExceptions(PARR_REST_US_017.getCode(), PARR_REST_US_017.getMessage(), BAD_REQUEST));

            Optional.of(user)
                    .map(User::getEmail)
                    .filter(s -> !StringUtils.isBlank(s))
                    .orElseThrow(() -> new ParrotExceptions(PARR_REST_US_018.getCode(), PARR_REST_US_018.getMessage(), BAD_REQUEST));


        } catch (ParrotExceptions ex) {
            return Mono.error(ex);
        }

        return Mono.just(true);
    }

}
