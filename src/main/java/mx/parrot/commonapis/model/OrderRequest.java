package mx.parrot.commonapis.model;

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
public class OrderRequest implements Serializable {

    private static final long serialVersionUID = 4688242599582361864L;

    @JsonProperty("customer")
    private Customer customer;

    @JsonProperty("order")
    private Order order;

}
