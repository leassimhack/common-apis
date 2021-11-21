package mx.parrot.commonapis.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler {


    @ExceptionHandler({ParrotExceptions.class})
    public ResponseEntity<EmptyDataResponse> handlingBaasException(ParrotExceptions e) {
        log.error("==> Exception handler by ParrotExceptionHandler::: {}", e.getMessage());
        String message = StringUtils.isEmpty(e.getMessage()) ? "Ocurrió un problema al obtener la respuesta de la capa de business" : e.getMessage();
        String code = StringUtils.isEmpty(e.getCode()) ? "PARR-ERR-000" : e.getCode();
        String description = StringUtils.isEmpty(e.getDescription()) ? message : e.getDescription();
        return this.getResponseEntityOfEmptyDataResponse(e.getHttpStatus(), code, message, description);
    }

    private ResponseEntity<EmptyDataResponse> getResponseEntityOfEmptyDataResponse(HttpStatus httpStatus, String code, String message, String description) {

        EmptyDataResponse response = new EmptyDataResponse();
        Error error = new Error();
        List<Error> errorList = new ArrayList<>();
        error.setCode(code);
        error.setMessage(message);
        error.setLevel("ERROR");
        error.setDescription(!code.equalsIgnoreCase(String.valueOf(httpStatus.value())) ? code + " - " + message : description);
        errorList.add(error);

        response.setErrors(errorList);


        return new ResponseEntity(response, httpStatus);
    }

}
