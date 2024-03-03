package io.github.ngchinhow.kraken.websockets.model.method.echo;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractRequest;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
@NoArgsConstructor
public final class PingRequest extends AbstractRequest {

    {
        setMethod(MethodMetadata.MethodType.PING);
    }
}
