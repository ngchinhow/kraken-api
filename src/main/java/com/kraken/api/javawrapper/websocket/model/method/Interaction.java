package com.kraken.api.javawrapper.websocket.model.method;

import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.method.detail.AbstractParameter;
import com.kraken.api.javawrapper.websocket.model.method.detail.AbstractResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.List;

public abstract class Interaction {
    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static abstract class AbstractInteractionRequest extends AbstractRequest {
        private AbstractParameter params;

        public List<RequestIdentifier> toRequestIdentifiers(ZonedDateTime timestamp) {
            RequestIdentifier.Builder builder = super.toRequestIdentifier(timestamp).toBuilder();
            return params.getSymbols()
                .stream()
                .map(e -> builder
                    .channel(params.getChannel())
                    .symbol(e)
                    .build())
                .toList();
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static abstract class AbstractInteractionResponse extends AbstractResponse {
        private AbstractResult result;
        private Boolean success;
        private String error;

        @Override
        public RequestIdentifier toRequestIdentifier() {
            return super.toRequestIdentifier().toBuilder()
                .channel(result.getChannel())
                .symbol(result.getSymbol())
                .build();
        }
    }
}
