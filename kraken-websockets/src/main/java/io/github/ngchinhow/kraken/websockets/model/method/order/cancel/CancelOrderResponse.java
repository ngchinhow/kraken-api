package io.github.ngchinhow.kraken.websockets.model.method.order.cancel;

import io.github.ngchinhow.kraken.websockets.dto.request.CancelOrderRequestIdentifier;
import io.github.ngchinhow.kraken.websockets.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderOutput;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public final class CancelOrderResponse extends AbstractInteractionResponse<BaseOrderOutput> {

    {
        setMethod(MethodMetadata.MethodType.CANCEL_ORDER);
    }

    @Override
    public RequestIdentifier toRequestIdentifier() {
        final var result = getResult();
        return new CancelOrderRequestIdentifier(super.toRequestIdentifier())
            .toBuilder()
            .orderId(result.getOrderId())
            .orderUserReference(result.getOrderUserReference())
            .build();
    }
}
