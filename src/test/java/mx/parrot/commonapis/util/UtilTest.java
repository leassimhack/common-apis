package mx.parrot.commonapis.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static mx.parrot.commonapis.util.ConstantsEnum.X_PARROT_CLIENT_ID;
import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
class UtilTest {

    @Test
    void test_replaceMessage() {
        assertEquals(Util.replaceMessage("field[]", 1), "field[1]");
    }

    @Test
    void test_getHeader() {

        Map<String, String> headers = new HashMap<>();
        headers.put(X_PARROT_CLIENT_ID.getValue(), "123456");

        assertEquals(Util.getHeader(headers, X_PARROT_CLIENT_ID.getValue()), "123456");
    }

    @Test
    void getHeader_when_string_notMatch_returnEmpty() {

        Map<String, String> headers = new HashMap<>();
        headers.put(X_PARROT_CLIENT_ID.getValue(), "123456");

        assertEquals(Util.getHeader(headers, "X_PARROT_CLIENT"), "");
    }

}
