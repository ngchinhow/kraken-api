package io.github.ngchinhow.kraken.websockets.model.method.order.edit;

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
public final class EditOrderRequest extends AbstractInteractionRequest<EditOrderParameter> {
    {
        setMethod(MethodMetadata.MethodType.EDIT_ORDER);
    }
}
