package io.github.ngchinhow.kraken.websockets.model.method.order.batchcancel;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionRequest;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public final class BatchCancelOrdersRequest extends AbstractInteractionRequest<BatchCancelOrdersParameter> {

    {
        setMethod(MethodMetadata.MethodType.BATCH_CANCEL);
    }
}
