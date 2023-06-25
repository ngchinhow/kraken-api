package io.github.ngchinhow.kraken.websocket.model.method.channel.instrument;

import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelResult;

public final class InstrumentResult extends AbstractChannelResult {
    {
        this.setChannel(ChannelMetadata.ChannelType.INSTRUMENT);
    }
}