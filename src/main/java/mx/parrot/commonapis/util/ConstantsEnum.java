package mx.parrot.commonapis.util;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ConstantsEnum {


    X_PARROT_CLIENT_ID("X-Parrot-Client-Id"),

    X_USER_ID("X-User-Id"),

    X_PARROT_DEVICE("X-Parrot-Device"),

    CREATED("CREATED"),

    UPDATED("UPDATED"),

    CURRENCY_XXX("XXX"),

    CURRENCY_XTS("XTS");


    private String value;

}
