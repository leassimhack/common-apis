package mx.parrot.commonapis.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.parrot.commonapis.exception.ParrotExceptions;
import mx.parrot.commonapis.model.Amount;
import mx.parrot.commonapis.model.Customer;
import mx.parrot.commonapis.model.Order;
import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.ParrotRequest;
import mx.parrot.commonapis.model.Product;
import mx.parrot.commonapis.util.Util;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_001;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_002;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_003;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_004;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_005;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_006;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_007;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_008;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_009;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_010;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_011;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_013;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_014;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_016;
import static mx.parrot.commonapis.exception.ErrorCodes.PARR_REST_ORD_020;
import static mx.parrot.commonapis.util.ConstantsEnum.X_PARROT_CLIENT_ID;
import static mx.parrot.commonapis.util.ConstantsEnum.X_PARROT_DEVICE;
import static mx.parrot.commonapis.util.Util.replaceMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderValidation {

    public static Mono<Boolean> validateUpdateOrder(final ParrotRequest<OrderRequest> parrotRequest) {

        if (StringUtils.isBlank(parrotRequest.getUserId().toString())) {
            return Mono.error(new ParrotExceptions(PARR_REST_ORD_020.getCode(), PARR_REST_ORD_020.getMessage(), BAD_REQUEST));
        }

        return validateHeaders(parrotRequest.getHeaders())
                .flatMap(headerOk -> validateBody(parrotRequest.getBody()));

    }

    private static Mono<Boolean> validateHeaders(final Map<String, String> headers) {


        final String xParrotClientId = Util.getHeader(headers, X_PARROT_CLIENT_ID.getValue());
        final String xParrotDevice = Util.getHeader(headers, X_PARROT_DEVICE.getValue());

        if (StringUtils.isBlank(xParrotClientId)) {
            return Mono.error(new ParrotExceptions(PARR_REST_ORD_001.getCode(), PARR_REST_ORD_001.getMessage(), BAD_REQUEST));
        }

        if (StringUtils.isBlank(xParrotDevice)) {
            return Mono.error(new ParrotExceptions(PARR_REST_ORD_002.getCode(), PARR_REST_ORD_002.getMessage(), BAD_REQUEST));
        }

        return Mono.just(true);
    }

    private static Mono<Boolean> validateBody(final OrderRequest request) {


        try {

            Optional.of(request)
                    .map(OrderRequest::getCustomer)
                    .map(Customer::getFirstName)
                    .filter(s -> !StringUtils.isBlank(s))
                    .orElseThrow(() -> new ParrotExceptions(PARR_REST_ORD_003.getCode(), PARR_REST_ORD_003.getMessage(), BAD_REQUEST));

            Optional.of(request)
                    .map(OrderRequest::getCustomer)
                    .map(Customer::getLastName)
                    .filter(s -> !StringUtils.isBlank(s))
                    .orElseThrow(() -> new ParrotExceptions(PARR_REST_ORD_004.getCode(), PARR_REST_ORD_004.getMessage(), BAD_REQUEST));

            Optional.of(request)
                    .map(OrderRequest::getOrder)
                    .map(Order::getTotal_amount)
                    .map(Amount::getValue)
                    .filter(s -> !StringUtils.isBlank(s.toString()))
                    .orElseThrow(() -> new ParrotExceptions(PARR_REST_ORD_005.getCode(), PARR_REST_ORD_005.getMessage(), BAD_REQUEST));

            Optional.of(request)
                    .map(OrderRequest::getOrder)
                    .map(Order::getTotal_amount)
                    .map(Amount::getValue)
                    .map(value -> {
                        BigDecimal valueCompare = new BigDecimal("0.0");
                        if (value.compareTo(valueCompare) <= 0.0) {
                            throw new ParrotExceptions(PARR_REST_ORD_013.getCode(), PARR_REST_ORD_013.getMessage(), BAD_REQUEST);
                        }
                        return true;
                    });

            Optional.of(request)
                    .map(OrderRequest::getOrder)
                    .map(Order::getTotal_amount)
                    .map(Amount::getCurrency)
                    .filter(s -> !StringUtils.isBlank(s))
                    .orElseThrow(() -> new ParrotExceptions(PARR_REST_ORD_006.getCode(), PARR_REST_ORD_006.getMessage(), BAD_REQUEST));


        } catch (ParrotExceptions ex) {
            return Mono.error(ex);
        }

        return validateProducts(request);
    }

    private static Mono<Boolean> validateProducts(OrderRequest request) {

        AtomicInteger index = new AtomicInteger();


        try {

            List<Product> products = Optional.of(request)
                    .map(OrderRequest::getOrder)
                    .map(Order::getProducts)
                    .filter(products1 -> !products1.isEmpty())
                    .orElseThrow(() -> new ParrotExceptions(PARR_REST_ORD_011.getCode(), PARR_REST_ORD_011.getMessage(), BAD_REQUEST));


            products.forEach(product -> {

                int i = index.getAndIncrement();


                Optional.of(product)
                        .map(Product::getName)
                        .filter(name -> !StringUtils.isBlank(name))
                        .orElseThrow(() -> new ParrotExceptions(PARR_REST_ORD_007.getCode(), replaceMessage(PARR_REST_ORD_007.getMessage(), i), BAD_REQUEST));

                Optional.of(product)
                        .map(Product::getQuantity)
                        .filter(quantity -> !StringUtils.isBlank(quantity.toString()))
                        .orElseThrow(() -> new ParrotExceptions(PARR_REST_ORD_008.getCode(), replaceMessage(PARR_REST_ORD_008.getMessage(), i), BAD_REQUEST));

                Optional.of(product)
                        .map(Product::getQuantity)
                        .filter(quantity -> 0 != quantity)
                        .orElseThrow(() -> new ParrotExceptions(PARR_REST_ORD_016.getCode(), replaceMessage(PARR_REST_ORD_016.getMessage(), i), BAD_REQUEST));

                Optional.of(product)
                        .map(Product::getAmount)
                        .map(Amount::getValue)
                        .filter(s -> !StringUtils.isBlank(s.toString()))
                        .orElseThrow(() -> new ParrotExceptions(PARR_REST_ORD_009.getCode(), replaceMessage(PARR_REST_ORD_009.getMessage(), i), BAD_REQUEST));

                Optional.of(product)
                        .map(Product::getAmount)
                        .map(Amount::getValue)
                        .map(value -> {
                            BigDecimal valueCompare = new BigDecimal("0.0");
                            if (value.compareTo(valueCompare) <= 0.0) {
                                throw new ParrotExceptions(PARR_REST_ORD_014.getCode(), replaceMessage(PARR_REST_ORD_014.getMessage(), i), BAD_REQUEST);
                            }
                            return true;
                        });

                Optional.of(product)
                        .map(Product::getAmount)
                        .map(Amount::getCurrency)
                        .filter(s -> !StringUtils.isBlank(s))
                        .orElseThrow(() -> new ParrotExceptions(PARR_REST_ORD_010.getCode(), PARR_REST_ORD_010.getMessage(), BAD_REQUEST));

            });


        } catch (ParrotExceptions ex) {
            return Mono.error(ex);
        }

        return Mono.just(true);

    }

}
