package io.github.ngchinhow.kraken.websocket.model.method.channel;

import io.github.ngchinhow.kraken.websocket.model.method.AbstractResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractChannelResult implements AbstractResult {
    private String channel;
    private Boolean snapshot;
}
