package io.github.ngchinhow.kraken.websocket.model.method;

import io.github.ngchinhow.kraken.websocket.dto.request.RequestIdentifier;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter(value = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor(force = true)
public abstract class AbstractInteractionResponse<T extends AbstractResult> extends AbstractResponse {
    private final T result;
    private final Boolean success;
    private final String error;

    @Override
    public RequestIdentifier toRequestIdentifier() {
        //noinspection DataFlowIssue
        return super.toRequestIdentifier().toBuilder()
            .channel(result.getChannel())
            .symbol(result.getSymbol())
            .build();
    }
}