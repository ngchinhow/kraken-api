package io.github.ngchinhow.kraken.websocket.model.method.channel;

import io.github.ngchinhow.kraken.websocket.model.method.AbstractResult;
import lombok.*;

import java.util.List;

@Getter
@Setter(value = AccessLevel.PROTECTED)
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractChannelResult implements AbstractResult {
    private String channel;
    private Boolean snapshot;
    private List<String> warnings;
}
