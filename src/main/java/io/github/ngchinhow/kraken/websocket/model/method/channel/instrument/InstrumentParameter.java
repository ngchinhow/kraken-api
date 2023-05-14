package io.github.ngchinhow.kraken.websocket.model.method.channel.instrument;

import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelParameter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class InstrumentParameter extends AbstractChannelParameter {
    {
        this.setChannel(ChannelMetadata.ChannelType.INSTRUMENT);
    }
}