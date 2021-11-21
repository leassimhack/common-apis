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
public class Product implements Serializable {

    private static final long serialVersionUID = -1808573319388569406L;
    
    @JsonProperty("id_product")
    private String id_product;

    @JsonProperty("name")
    private String name;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("amount")
    private Amount amount;

}
