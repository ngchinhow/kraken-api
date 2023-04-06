package io.github.ngchinhow.kraken.websocket.model.method;

import io.github.ngchinhow.kraken.websocket.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websocket.enums.MethodMetadata;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

public class Echo {
    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @Jacksonized
    @NoArgsConstructor
    public static class PingRequest extends AbstractRequest {
        {
            this.setMethod(MethodMetadata.MethodType.PING);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @Jacksonized
    @NoArgsConstructor
    public static class PongResponse extends AbstractResponse {
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
}
