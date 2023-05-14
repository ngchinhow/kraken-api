package io.github.ngchinhow.kraken.websocket.model.method;

import io.github.ngchinhow.kraken.websocket.dto.request.RequestIdentifier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractInteractionRequest extends AbstractRequest {
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