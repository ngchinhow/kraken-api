package io.github.ngchinhow.kraken.websocket.model.method;

import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter(value = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractInteractionResponse<T> extends AbstractResponse {
    private T result;
    private Boolean success;
    private String error;
}