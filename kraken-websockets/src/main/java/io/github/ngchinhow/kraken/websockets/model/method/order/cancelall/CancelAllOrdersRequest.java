package io.github.ngchinhow.kraken.websockets.model.method.order.cancelall;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderParameter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public final class CancelAllOrdersRequest extends AbstractInteractionRequest<BaseOrderParameter> {

    {
        setMethod(MethodMetadata.MethodType.CANCEL_ALL);
    }
}
