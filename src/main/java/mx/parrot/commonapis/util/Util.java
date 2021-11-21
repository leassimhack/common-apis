package mx.parrot.commonapis.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Util {

    private final static String TEXT_SEARCH_REPLACE = "[]";

    public static String getHeader(Map<String, String> headers, String key) {
        return Optional.ofNullable(headers).map(Map::entrySet).map((entries) -> {
            Optional<String> optionalHeader = entries.stream().filter((f) -> f.getKey().equalsIgnoreCase(key)).findAny().map(Map.Entry::getValue);
            return optionalHeader.orElse("");
        }).orElse("");
    }

    public static String replaceMessage(final String description, final Integer index) {
        return description.replace(TEXT_SEARCH_REPLACE, "[" + index + "]");
    }

    public static Integer getKey(final Integer userId) {

        final Long time = new Date().getTime();

        return time.intValue() + userId;

    }
}
