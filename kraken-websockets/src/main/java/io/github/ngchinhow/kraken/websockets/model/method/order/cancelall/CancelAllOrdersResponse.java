package io.github.ngchinhow.kraken.websockets.model.method.order.cancelall;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseCountResult;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public final class CancelAllOrdersResponse extends AbstractInteractionResponse<BaseCountResult> {

    {
        setMethod(MethodMetadata.MethodType.CANCEL_ALL);
    }
}
