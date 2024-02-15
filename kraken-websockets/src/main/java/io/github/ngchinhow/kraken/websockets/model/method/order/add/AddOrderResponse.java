package io.github.ngchinhow.kraken.websockets.model.method.order.add;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderOutput;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
public final class AddOrderResponse extends AbstractInteractionResponse<BaseOrderOutput> {

    {
        setMethod(MethodMetadata.MethodType.ADD_ORDER);
    }
}
