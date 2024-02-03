package io.github.ngchinhow.kraken.websockets.model.method.channel.instrument;

import io.github.ngchinhow.kraken.websockets.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelResult;

public final class InstrumentResult extends AbstractChannelResult {
    {
        this.setChannel(ChannelMetadata.ChannelType.INSTRUMENT);
    }
}