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
public class BaseOrderParameter implements PrivateParameterInterface {
    private String token;
}