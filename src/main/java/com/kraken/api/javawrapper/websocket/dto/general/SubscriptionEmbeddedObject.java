package com.kraken.api.javawrapper.websocket.dto.general;

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
        public Builder depth(Integer depth) {
            if (Objects.nonNull(name)) {
                if (Objects.nonNull(depth) && !name.equals(WebSocketEnumerations.CHANNEL.BOOK))
                    throw new RuntimeException(
                        "The depth parameter within the Subscription object is only used for subscriptions to the Book channel"
                    );
                else
                    this.depth = depth;
            }
            return this;
        }

        public Builder interval(Integer interval) {
            if (Objects.nonNull(name)) {
                if (Objects.nonNull(interval) && !name.equals(WebSocketEnumerations.CHANNEL.OHLC))
                    throw new RuntimeException(
                        "The interval parameter within the Subscription object is only used for subscriptions to the OHLC channel"
                    );
                else
                    this.interval = interval;
            }
            return this;
        }

        public Builder rateCounter(Boolean rateCounter) {
            if (Objects.nonNull(name)) {
                if (Objects.nonNull(rateCounter) && !name.equals(WebSocketEnumerations.CHANNEL.OPEN_ORDERS))
                    throw new RuntimeException(
                        "The ratecounter parameter within the Subscription object is only used for subscriptions to the Open Orders channel"
                    );
                else
                    this.rateCounter = rateCounter;
            }
            return this;
        }

        public Builder snapshot(Boolean snapshot) {
            if (Objects.nonNull(name)) {
                if (Objects.nonNull(snapshot) && !name.equals(WebSocketEnumerations.CHANNEL.OWN_TRADES))
                    throw new RuntimeException(
                        "The snapshot parameter within the Subscription object is only used for subscriptions to the Own Trades channel"
                    );
                else
                    this.snapshot = snapshot;
            }
            return this;
        }

        public Builder consolidateTaker(Boolean consolidateTaker) {
            if (Objects.nonNull(name)) {
                if (Objects.nonNull(consolidateTaker) && !name.equals(WebSocketEnumerations.CHANNEL.OWN_TRADES))
                    throw new RuntimeException(
                        "The consolidateTaker parameter within the Subscription object is only used for subscriptions to the Own Trades channel"
                    );
                else
                    this.consolidateTaker = consolidateTaker;
            }
            return this;
        }
    }
}
