package io.github.ngchinhow.kraken.websockets.model.method.channel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AbstractChannelParameter implements ChannelParameterInterface {
    private String channel;
    private Boolean snapshot;
}
