package io.github.ngchinhow.kraken.websocket.model.method.detail.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import lombok.*;
import lombok.experimental.SuperBuilder;

public abstract class ExecutionsSubscription {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor(force = true)
    public static class Parameter extends AbstractChannelSubscription.Parameter {
        @JsonProperty("snapshot_trades")
        private Boolean snapshotTrades;
        @JsonProperty("order_status")
        private Boolean orderStatus;
        @JsonProperty("ratecounter")
        private Boolean rateCounter;
        @NonNull
        private String token;

        {
            this.setChannel(ChannelMetadata.ChannelType.EXECUTIONS);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor(force = true)
    public static class Result extends AbstractChannelSubscription.Result {
        @JsonProperty("snapshot_trades")
        private Boolean snapshotTrades;
        @JsonProperty("order_status")
        private Boolean orderStatus;
        @JsonProperty("ratecounter")
        private Boolean rateCounter;
        @NonNull
        private String token;

        {
            this.setChannel(ChannelMetadata.ChannelType.EXECUTIONS);
        }
    }
}
