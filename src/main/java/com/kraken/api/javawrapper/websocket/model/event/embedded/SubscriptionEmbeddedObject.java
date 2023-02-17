package com.kraken.api.javawrapper.websocket.model.event.embedded;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.Objects;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionEmbeddedObject {
    private Integer depth;
    private Integer interval;
    private WebSocketEnumerations.CHANNEL name;
    @JsonProperty("ratecounter")
    private Boolean rateCounter;
    private Boolean snapshot;
    private String token;
    @JsonProperty("consolidate_taker")
    private Boolean consolidateTaker;

    @SuppressWarnings("unused")
    public static class Builder {
        public SubscriptionEmbeddedObject build() {
            if (Objects.nonNull(this.snapshot) && !this.name.equals(WebSocketEnumerations.CHANNEL.OWN_TRADES))
                throw new RuntimeException("The snapshot parameter within the Subscription object is only used for " +
                    "subscriptions to the Own Trades channel");
            if (Objects.nonNull(this.consolidateTaker) && !this.name.equals(WebSocketEnumerations.CHANNEL.OWN_TRADES))
                throw new RuntimeException("The consolidateTaker parameter within the Subscription object is only " +
                    "used for subscriptions to the Own Trades channel");
            if (Objects.nonNull(this.rateCounter) && !this.name.equals(WebSocketEnumerations.CHANNEL.OPEN_ORDERS))
                throw new RuntimeException("The ratecounter parameter within the Subscription object is only used " +
                    "for subscriptions to the Open Orders channel");
            if (Objects.nonNull(this.interval) && !this.name.equals(WebSocketEnumerations.CHANNEL.OHLC))
                throw new RuntimeException("The interval parameter within the Subscription object is only used for " +
                    "subscriptions to the OHLC channel");
            if (Objects.nonNull(this.depth) && !this.name.equals(WebSocketEnumerations.CHANNEL.BOOK))
                throw new RuntimeException("The depth parameter within the Subscription object is only used for " +
                    "subscriptions to the Book channel");

            return new SubscriptionEmbeddedObject(
                this.depth,
                this.interval,
                this.name,
                this.rateCounter,
                this.snapshot,
                this.token,
                this.consolidateTaker
            );
        }
    }
}
