package mx.parrot.commonapis.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;


@Getter
@Setter
public class ParrotExceptions extends RuntimeException {

    private String code;
    private String message;
    private String description;
    private List<Error> errors;
    private HttpStatus httpStatus;

    public ParrotExceptions() {
        super();
    }

    public ParrotExceptions(String code, String customMessage, String description, List<Error> errors, HttpStatus httpStatus) {
        this.code = code;
        this.message = customMessage;
        this.description = description;
        this.errors = CollectionUtils.isEmpty(errors) ? null : Collections.unmodifiableList(errors);
        this.httpStatus = httpStatus;
    }

    public ParrotExceptions(String code, String customMessage, HttpStatus httpStatus) {
        this.code = code;
        this.message = customMessage;
        this.httpStatus = httpStatus;
    }

}

