package mx.parrot.commonapis.transform;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
class LocalDateTimePersistenceConverterTest {

    @Test
    void test_convertToDatabaseColumn() {

        LocalDateTimePersistenceConverter converter = new LocalDateTimePersistenceConverter();

        assertNotNull(converter.convertToDatabaseColumn(LocalDateTime.now()));
    }

    @Test
    void test_convertToEntityAttribute() {

        LocalDateTimePersistenceConverter converter = new LocalDateTimePersistenceConverter();

        assertNotNull(converter.convertToEntityAttribute(Timestamp.valueOf(LocalDateTime.now())));
    }


}
