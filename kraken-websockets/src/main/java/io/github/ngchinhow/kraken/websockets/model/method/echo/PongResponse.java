package io.github.ngchinhow.kraken.websockets.model.method.echo;

import io.github.ngchinhow.kraken.websockets.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractResponse;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public final class PongResponse extends AbstractResponse {
    {
        setMethod(MethodMetadata.MethodType.PONG);
    }

    @Override
    public RequestIdentifier toRequestIdentifier() {
        return super.toRequestIdentifier()
                    .toBuilder()
                    .method(MethodMetadata.MethodType.PING)
                    .build();
    }
}
