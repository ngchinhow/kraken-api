package io.github.ngchinhow.kraken.websocket.model.method.channel;

import io.github.ngchinhow.kraken.websocket.model.method.AbstractParameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractChannelParameter implements AbstractParameter {
    private String channel;
    private Boolean snapshot;
}