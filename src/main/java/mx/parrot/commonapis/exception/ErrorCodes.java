package mx.parrot.commonapis.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ErrorCodes {

    PARR_REST_ORD_000("PARR_REST_ORD_000", "Error, the server encountered an unexpected condition that prevented it from fulfilling the request"),

    PARR_REST_ORD_001("PARR_REST_ORD_001", "Error, the header X-Parrot-Client-Id must not be null or empty"),

    PARR_REST_ORD_002("PARR_REST_ORD_002", "Error, the header X-Parrot-Device must not be null or empty"),

    PARR_REST_ORD_003("PARR_REST_ORD_003", "Error, the field 'customer.full_name' must not be null"),

    PARR_REST_ORD_004("PARR_REST_ORD_004", "Error, the product id  dont exist"),

    PARR_REST_ORD_005("PARR_REST_ORD_005", "Error, the field 'order.total_amount.value' must not be null"),

    PARR_REST_ORD_006("PARR_REST_ORD_006", "Error, the field 'order.total_amount.currency' must not be null"),

    PARR_REST_ORD_007("PARR_REST_ORD_007", "Error, the field 'order.products[].name' must not be null"),

    PARR_REST_ORD_008("PARR_REST_ORD_008", "Error, the field 'order.products[].quantity' must not be null"),

    PARR_REST_ORD_009("PARR_REST_ORD_009", "Error, the field 'order.products[].amount.value' must not be null"),

    PARR_REST_ORD_010("PARR_REST_ORD_010", "Error, the field 'order.products[].amount.currency' must not be null"),

    PARR_REST_ORD_011("PARR_REST_ORD_011", "Error, the list of 'order.products' must not be null or empty"),

    PARR_REST_ORD_012("PARR_REST_ORD_012", "Error, the value to the field 'order.products[].name' is incorrect"),

    PARR_REST_ORD_013("PARR_REST_ORD_013", "Error, the value to the field 'order.total_amount.value' is incorrect"),

    PARR_REST_ORD_014("PARR_REST_ORD_014", "Error, the value to the field 'order.products[].amount.value' is incorrect"),

    PARR_REST_ORD_015("PARR_REST_ORD_015", "Error, the field 'idempotentReference' must not be null or empty"),

    PARR_REST_ORD_016("PARR_REST_ORD_016", "Error, the value to the field 'order.products[].quantity' is incorrect"),

    PARR_REST_US_017("PARR_REST_ORD_017", "Error, the field 'name' must not be null"),

    PARR_REST_US_018("PARR_REST_ORD_018", "Error, the field 'email' must not be null"),

    PARR_REST_US_019("PARR_REST_ORD_019", "Error, the 'email' entered already exists"),

    PARR_REST_ORD_020("PARR_REST_ORD_020", "Error, the header X-User-Id must not be null or empty"),

    PARR_REST_ORD_021("PARR_REST_ORD_021", "Error, the value header X-User-Id is incorrect"),

    PARR_REST_ORD_022("PARR_REST_ORD_022", "Error, the order id  dont exist"),

    PARR_REST_ORD_023("PARR_REST_ORD_023", " the value of path param '{order_id}' is incorrect"),

    PARR_REST_ORD_024("PARR_REST_ORD_024", " the value of path param '{product_id}' is incorrect"),

    PARR_REST_ORD_025("PARR_REST_ORD_025", "Error, the field 'name' must not be null"),

    PARR_REST_ORD_026("PARR_REST_ORD_026", "Error, the field 'quantity' must not be null"),

    PARR_REST_ORD_027("PARR_REST_ORD_027", "Error, the value to the field 'quantity' is incorrect"),

    PARR_REST_ORD_028("PARR_REST_ORD_028", "Error, the field 'amount.value' must not be null"),

    PARR_REST_ORD_029("PARR_REST_ORD_029", "Error, the value to the field 'amount.value' is incorrect"),

    PARR_REST_ORD_030("PARR_REST_ORD_030", "Error, the field 'amount.currency' must not be null"),

    PARR_REST_ORD_031("PARR_REST_ORD_031", "Error, this register is already exists to this order, please validate your information"),

    PARR_REST_ORD_032("PARR_REST_ORD_032", "Error, the value of field 'amount.currency' is incorrect"),

    PARR_REST_ORD_033("PARR_REST_ORD_033", "Error, the value of field 'order.products[].amount.currency' is incorrect");

    private String code;

    private String message;

}
