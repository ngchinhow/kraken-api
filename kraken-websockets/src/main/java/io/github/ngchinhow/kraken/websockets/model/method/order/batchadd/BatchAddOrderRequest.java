package io.github.ngchinhow.kraken.websockets.model.method.order.batchadd;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
public final class BatchAddOrderRequest extends AbstractInteractionRequest<BatchAddOrderParameter> {

    {
        this.setMethod(MethodMetadata.MethodType.BATCH_ADD);
    }
}
