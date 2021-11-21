package mx.parrot.commonapis.model;

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
public class Order implements Serializable {

    private static final long serialVersionUID = 4688242599582361864L;
    
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("total_amount")
    private Amount total_amount;

    @JsonProperty("products")
    private List<Product> products;

}
