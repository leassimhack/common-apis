package mx.parrot.commonapis.dao.entity;


import lombok.Data;
import mx.parrot.commonapis.transform.LocalDateTimePersistenceConverter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Table("products")
public class Products implements Serializable {

    private static final long serialVersionUID = -3319997576677814092L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    private String name;
    private double quantity;
    private double amount;
    private String currency;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime createdTime;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime updateTime;
    private Integer idOrder;

}