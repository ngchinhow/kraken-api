package io.github.ngchinhow.kraken.websockets.model.method.order.cancelallafter;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionResponse;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public final class CancelAllOrdersAfterResponse extends AbstractInteractionResponse<CancelAllOrdersAfterResult> {

    {
        setMethod(MethodMetadata.MethodType.CANCEL_ALL_ORDERS_AFTER);
    }
}
