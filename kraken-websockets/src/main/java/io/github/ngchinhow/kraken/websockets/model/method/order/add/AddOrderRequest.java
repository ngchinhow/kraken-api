package io.github.ngchinhow.kraken.websockets.model.method.order.add;


import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Jacksonized
public final class AddOrderRequest extends AbstractInteractionRequest<AddOrderParameter> {

    {
        setMethod(MethodMetadata.MethodType.ADD_ORDER);
    }
}
