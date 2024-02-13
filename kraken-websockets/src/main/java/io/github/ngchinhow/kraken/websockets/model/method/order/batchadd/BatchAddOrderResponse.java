package io.github.ngchinhow.kraken.websockets.model.method.order.batchadd;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderOutput;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
public final class BatchAddOrderResponse extends AbstractInteractionResponse<List<BaseOrderOutput>> {
    {
        this.setMethod(MethodMetadata.MethodType.BATCH_ADD);
    }
}
