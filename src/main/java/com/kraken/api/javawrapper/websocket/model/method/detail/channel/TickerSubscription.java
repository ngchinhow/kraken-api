package com.kraken.api.javawrapper.websocket.model.method.detail.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.enums.ChannelMetadata;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

public abstract class TickerSubscription {
    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Parameter extends AbstractChannelSubscription.Parameter {
        @JsonProperty(value = "symbol", required = true)
        private List<String> symbols;

        {
            this.setChannel(ChannelMetadata.ChannelType.TICKER);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor(force = true)
    public static class Result extends AbstractChannelSubscription.Result {
        @NonNull
        private String symbol;

        {
            this.setChannel(ChannelMetadata.ChannelType.TICKER);
        }
    }
}