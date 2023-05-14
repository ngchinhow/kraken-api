package io.github.ngchinhow.kraken.websocket.model.method.unsubscription;

import io.github.ngchinhow.kraken.websocket.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractInteractionRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
public class UnsubscribeRequest extends AbstractInteractionRequest {
    {
        this.setMethod(MethodMetadata.MethodType.UNSUBSCRIBE);
    }
}