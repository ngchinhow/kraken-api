package io.github.ngchinhow.kraken.websockets.model.method.order.edit;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionResponse;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public final class EditOrderResponse extends AbstractInteractionResponse<EditOrderResult> {

    {
        setMethod(MethodMetadata.MethodType.EDIT_ORDER);
    }
}
