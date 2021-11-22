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
public class OrderResponse implements Serializable {

    private static final long serialVersionUID = 4688242599582361864L;

    @JsonProperty("customer")
    private Customer customer;

    @JsonProperty("order")
    private Order order;

    @JsonProperty("status")
    private String status;

    @JsonProperty("create_time")
    private LocalDateTime create_time;

    @JsonProperty("update_time")
    private LocalDateTime update_time;

}
