package io.github.ngchinhow.kraken.websocket.model.method.order;

import io.github.ngchinhow.kraken.websocket.model.method.AbstractParameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractOrderParameter implements AbstractParameter {
    private String token;
}