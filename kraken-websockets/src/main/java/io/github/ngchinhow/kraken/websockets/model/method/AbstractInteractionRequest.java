package io.github.ngchinhow.kraken.websockets.model.method;

import io.github.ngchinhow.kraken.websockets.dto.request.RequestIdentifier;
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
public abstract class AbstractInteractionRequest<T extends ParameterInterface> extends AbstractRequest {
    private T params;

    public List<RequestIdentifier> toRequestIdentifiers(ZonedDateTime timestamp) {
        var requestIdentifier = super.toRequestIdentifier(timestamp);
        return List.of(requestIdentifier);
    }
}