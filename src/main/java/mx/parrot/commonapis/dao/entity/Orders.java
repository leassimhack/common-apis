package mx.parrot.commonapis.dao.entity;


import lombok.Data;
import mx.parrot.commonapis.transform.LocalDateTimePersistenceConverter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Convert;
import javax.persistence.Transient;
import java.time.LocalDateTime;


@Data
@Table("orders")
public class Orders implements Persistable<Integer> {

    @Id
    private Integer id;
    private String nameCustomer;
    private double totalAmount;
    private String currency;
    private String status;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime createdTime;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime updateTime;
    private int idUser;

    @Override
    @Transient
    public boolean isNew() {

        return  status.equals("CREATED");
    }

}