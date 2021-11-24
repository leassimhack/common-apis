package mx.parrot.commonapis.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product implements Serializable {

    private static final long serialVersionUID = -1808573319388569406L;

    @JsonProperty("id_product")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("amount")
    private Amount amount;

    @JsonProperty("status")
    private String status;

    @JsonProperty("create_time")
    private LocalDateTime create_time;

    @JsonProperty("update_time")
    private LocalDateTime update_time;

}
