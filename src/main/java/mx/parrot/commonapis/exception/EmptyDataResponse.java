package mx.parrot.commonapis.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class EmptyDataResponse implements Serializable {

    private static final long serialVersionUID = 6686211963274276867L;

    @JsonProperty("errors")
    private List<Error> errors;

}