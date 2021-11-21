package mx.parrot.commonapis.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Error implements Serializable {

    private static final long serialVersionUID = 6120691971784445989L;
    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("level")
    private String level;

    @JsonProperty("description")
    private String description;
}
