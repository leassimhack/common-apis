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
@Table("users")
public class User implements Serializable {

    private static final long serialVersionUID = -3319997576677814092L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    private String name;
    private String email;
    private String password;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime createdTime;

}