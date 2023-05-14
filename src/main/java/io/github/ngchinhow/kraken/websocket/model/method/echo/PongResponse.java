package io.github.ngchinhow.kraken.websocket.model.method.echo;

import io.github.ngchinhow.kraken.websocket.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websocket.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractResponse;
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
public class PongResponse extends AbstractResponse {
    {
        this.setMethod(MethodMetadata.MethodType.PONG);
    }

    @Override
    public RequestIdentifier toRequestIdentifier() {
        return super.toRequestIdentifier().toBuilder()
            .method(MethodMetadata.MethodType.PING)
            .build();
    }
}
