package mx.parrot.commonapis.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;
import java.util.Map;


@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ParrotRequest<T> implements Serializable {

    private static final long serialVersionUID = 1428874191414459914L;

    private T body;

    private Map<String,String> headers;

    private Map<String,String> params;

    private Integer userId;

    private UriComponentsBuilder builder;

}