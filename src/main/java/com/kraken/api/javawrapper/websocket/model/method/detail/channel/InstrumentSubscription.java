package com.kraken.api.javawrapper.websocket.model.method.detail.channel;

import com.kraken.api.javawrapper.websocket.enums.ChannelMetadata;

public abstract class InstrumentSubscription {

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
