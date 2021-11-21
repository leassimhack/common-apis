package mx.parrot.commonapis.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_001;
import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CustomizedResponseEntityExceptionHandlerTest {

    @Test
    void createOrUpdateOrder_expectedOk() {

        final CustomizedResponseEntityExceptionHandler handler = new CustomizedResponseEntityExceptionHandler();

        ResponseEntity<EmptyDataResponse> response = handler.handlingBaasException(new ParrotExceptions(PARR_REST_ORD_001.getCode(), PARR_REST_ORD_001.getMessage(), HttpStatus.BAD_REQUEST));

        assertNotNull(response);

        assertNotNull(response.getStatusCode());
        assertNotNull(response.getBody().getErrors());
        assertNotNull(response.getBody().toString());
        assertNotNull(response.getBody().getErrors().get(0).getMessage());
        assertNotNull(response.getBody().getErrors().get(0).getCode());
        assertNotNull(response.getBody().getErrors().get(0).getDescription());
        assertNotNull(response.getBody().getErrors().get(0).getLevel());


    }
}
