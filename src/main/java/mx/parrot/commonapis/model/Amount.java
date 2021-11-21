package mx.parrot.commonapis.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Amount implements Serializable {

    private static final long serialVersionUID = 1428874191414459914L;

    @JsonProperty("value")
    private BigDecimal value;

    @JsonProperty("currency")
    private String currency;

}
