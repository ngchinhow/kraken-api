package com.kraken.api.javawrapper.websocket.model.method.detail.channel;

import com.kraken.api.javawrapper.websocket.enums.ChannelMetadata;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public abstract class InstrumentSubscription {

    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @NoArgsConstructor
    public static class Parameter extends AbstractChannelSubscription.Parameter {
        {
            this.setChannel(ChannelMetadata.ChannelType.INSTRUMENT);
        }
    }

    public static class Result extends AbstractChannelSubscription.Result {
        {
            this.setChannel(ChannelMetadata.ChannelType.INSTRUMENT);
        }
    }
}
