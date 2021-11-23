package mx.parrot.commonapis.dao.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.parrot.commonapis.transform.LocalDateTimePersistenceConverter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;
import java.time.LocalDateTime;


@Data
@Table("products")
@Getter
@Setter
public class Products implements Persistable<Integer> {



    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    private String name;
    private Integer quantity;
    private double amount;
    private String currency;
    private String status;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime createdTime;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime updateTime;
    private Integer idOrder;

    @Override
    @Transient
    public boolean isNew() {

        return  status.equals("CREATED");
    }

}