package io.github.ngchinhow.kraken.websocket.model.method.echo;

import io.github.ngchinhow.kraken.websocket.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractRequest;
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
public class PingRequest extends AbstractRequest {
    {
        this.setMethod(MethodMetadata.MethodType.PING);
    }
}