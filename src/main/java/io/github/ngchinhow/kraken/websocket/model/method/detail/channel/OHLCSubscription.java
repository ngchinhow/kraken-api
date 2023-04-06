package io.github.ngchinhow.kraken.websocket.model.method.detail.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

public abstract class OHLCSubscription {
    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor(force = true)
    public static class Parameter extends AbstractChannelSubscription.Parameter {
        @JsonProperty(value = "symbol", required = true)
        private List<String> symbols;
        @NonNull
        private Integer interval;

        {
            this.setChannel(ChannelMetadata.ChannelType.OHLC);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor(force = true)
    public static class Result extends AbstractChannelSubscription.Result {
        @NonNull
        private String symbol;
        @NonNull
        private Integer interval;

        {
            this.setChannel(ChannelMetadata.ChannelType.OHLC);
        }
    }
}
