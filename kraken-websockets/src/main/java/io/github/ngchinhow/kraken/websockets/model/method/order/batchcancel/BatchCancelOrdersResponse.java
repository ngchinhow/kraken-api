package io.github.ngchinhow.kraken.websockets.model.method.order.batchcancel;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.ObjectUtils;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public final class BatchCancelOrdersResponse extends AbstractInteractionResponse<ObjectUtils.Null> {
    @JsonProperty(value = "orders_cancelled")
    private Integer ordersCancelled;

    {
        setMethod(MethodMetadata.MethodType.BATCH_CANCEL);
    }
}
