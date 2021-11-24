package mx.parrot.commonapis.transform;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mx.parrot.commonapis.model.OrderRequest;
import mx.parrot.commonapis.model.ParrotRequest;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static mx.parrot.commonapis.util.ConstantsEnum.X_PARROT_CLIENT_ID;
import static mx.parrot.commonapis.util.ConstantsEnum.X_PARROT_DEVICE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransformApiToService {


    public static Mono<ParrotRequest<OrderRequest>> transformUpdateOrderToService(
            final OrderRequest orderRequest,
            final String xParrotClientId,
            final Integer userID,
            final String xParrotDevice) {

        Map<String, String> headers = new HashMap<>();

        headers.put(X_PARROT_CLIENT_ID.getValue(), xParrotClientId);
        headers.put(X_PARROT_DEVICE.getValue(), xParrotDevice);


        return Mono.just(new ParrotRequest<OrderRequest>()
                .setBody(orderRequest)
                .setHeaders(headers)
                .setUserId(userID)
        );
    }

}
