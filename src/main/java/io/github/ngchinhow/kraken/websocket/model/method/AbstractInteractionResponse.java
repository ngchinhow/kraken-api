package io.github.ngchinhow.kraken.websocket.model.method;

import io.github.ngchinhow.kraken.websocket.dto.request.RequestIdentifier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractInteractionResponse<T extends AbstractResult> extends AbstractResponse {
    private T result;
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