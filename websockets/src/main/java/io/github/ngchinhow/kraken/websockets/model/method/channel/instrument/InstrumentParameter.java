package io.github.ngchinhow.kraken.websockets.model.method.channel.instrument;

import io.github.ngchinhow.kraken.websockets.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelParameter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public final class InstrumentParameter extends AbstractChannelParameter {
    {
        this.setChannel(ChannelMetadata.ChannelType.INSTRUMENT);
    }
}