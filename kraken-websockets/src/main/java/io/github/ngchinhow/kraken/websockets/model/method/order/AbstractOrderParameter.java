package io.github.ngchinhow.kraken.websockets.model.method.order;

import io.github.ngchinhow.kraken.websockets.model.method.PrivateParameterInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractOrderParameter implements PrivateParameterInterface {
    private String token;
}