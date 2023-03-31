package com.kraken.api.javawrapper.websocket.model.method;

import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import static com.kraken.api.javawrapper.websocket.enums.MethodMetadata.MethodType.PING;
import static com.kraken.api.javawrapper.websocket.enums.MethodMetadata.MethodType.PONG;

public class Echo {
    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @Jacksonized
    @NoArgsConstructor
    public static class PingRequest extends AbstractRequest {
        {
            this.setMethod(PING);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @Jacksonized
    @NoArgsConstructor
    public static class PongResponse extends AbstractResponse {
        {
            this.setMethod(PONG);
        }

        @Override
        public RequestIdentifier toRequestIdentifier() {
            return super.toRequestIdentifier().toBuilder()
                .method(PING)
                .build();
        }
    }
}
