package io.github.ngchinhow.kraken.websockets.model.method.order.cancel;

import io.github.ngchinhow.kraken.websockets.dto.request.CancelOrderRequestIdentifier;
import io.github.ngchinhow.kraken.websockets.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionRequest;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Jacksonized
public final class CancelOrderRequest extends AbstractInteractionRequest<CancelOrderParameter> {

    {
        setMethod(MethodMetadata.MethodType.CANCEL_ORDER);
    }

    @Override
    public List<RequestIdentifier> toRequestIdentifiers(ZonedDateTime serverTime) {
        final var requestIdentifier = super.toRequestIdentifier(serverTime);
        final var list = new ArrayList<RequestIdentifier>();
        List<String> orderIds;
        List<BigInteger> orderUserReferences;
        if ((orderIds = getParams().getOrderIds()) != null)
            list.addAll(orderIds.stream()
                                .map(id -> new CancelOrderRequestIdentifier(requestIdentifier)
                                    .toBuilder()
                                    .orderId(id)
                                    .build())
                                .toList());
        if ((orderUserReferences = getParams().getOrderUserReferences()) != null)
            list.addAll(orderUserReferences.stream()
                                           .map(reference -> new CancelOrderRequestIdentifier(requestIdentifier)
                                               .toBuilder()
                                               .orderUserReference(reference)
                                               .build())
                                           .toList());
        return list;
    }
}
