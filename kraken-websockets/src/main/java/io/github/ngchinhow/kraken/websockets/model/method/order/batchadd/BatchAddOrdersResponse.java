package io.github.ngchinhow.kraken.websockets.model.method.order.batchadd;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderOutput;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@SuperBuilder
@Jacksonized
public final class BatchAddOrdersResponse extends AbstractInteractionResponse<List<BaseOrderOutput>> {
    {
        setMethod(MethodMetadata.MethodType.BATCH_ADD);
    }
}
