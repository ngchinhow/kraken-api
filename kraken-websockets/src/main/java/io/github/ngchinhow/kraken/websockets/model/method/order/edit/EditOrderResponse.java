package io.github.ngchinhow.kraken.websockets.model.method.order.edit;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter(value = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public final class EditOrderResponse extends AbstractInteractionResponse<EditOrderResult> {

    {
        setMethod(MethodMetadata.MethodType.EDIT_ORDER);
    }
}
