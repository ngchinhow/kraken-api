package io.github.ngchinhow.kraken.websocket.model.method.subscription;

import io.github.ngchinhow.kraken.websocket.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractInteractionRequest;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractParameter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
public class SubscribeRequest<T extends AbstractParameter> extends AbstractInteractionRequest<T> {

    {
        this.setMethod(MethodMetadata.MethodType.SUBSCRIBE);
    }
}
