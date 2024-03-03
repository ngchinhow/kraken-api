package io.github.ngchinhow.kraken.websockets.model.method.order.cancelallafter;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionRequest;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public final class CancelAllOrdersAfterRequest extends AbstractInteractionRequest<CancelAllOrdersAfterParameter> {

    {
        setMethod(MethodMetadata.MethodType.CANCEL_ALL_ORDERS_AFTER);
    }
}
