package org.trading.krakenapi.websocket.dto.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.trading.krakenapi.websocket.enums.WebSocketEnumerations;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
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

    public static class Builder {
        public Builder depth(Integer depth) {
            if (ObjectUtils.isNotEmpty(depth) && !name.equals(WebSocketEnumerations.CHANNEL.BOOK))
                throw new RuntimeException(
                    "The depth parameter within the Subscription object is only used for subscriptions to the Book channel"
                );
            this.depth = depth;
            return this;
        }

        public Builder interval(Integer interval) {
            if (ObjectUtils.isNotEmpty(interval) && !name.equals(WebSocketEnumerations.CHANNEL.OHLC))
                throw new RuntimeException(
                    "The interval parameter within the Subscription object is only used for subscriptions to the OHLC channel"
                );
            this.interval = interval;
            return this;
        }

        public Builder rateCounter(Boolean rateCounter) {
            if (ObjectUtils.isNotEmpty(rateCounter) && !name.equals(WebSocketEnumerations.CHANNEL.OPEN_ORDERS))
                throw new RuntimeException(
                    "The ratecounter parameter within the Subscription object is only used for subscriptions to the Open Orders channel"
                );
            this.rateCounter = rateCounter;
            return this;
        }

        public Builder snapshot(Boolean snapshot) {
            if (ObjectUtils.isNotEmpty(snapshot) && !name.equals(WebSocketEnumerations.CHANNEL.OWN_TRADES))
                throw new RuntimeException(
                    "The snapshot parameter within the Subscription object is only used for subscriptions to the Own Trades channel"
                );
            this.snapshot = snapshot;
            return this;
        }

        public Builder consolidateTaker(Boolean consolidateTaker) {
            if (ObjectUtils.isNotEmpty(consolidateTaker) && !name.equals(WebSocketEnumerations.CHANNEL.OWN_TRADES))
                throw new RuntimeException(
                    "The consolidateTaker parameter within the Subscription object is only used for subscriptions to the Own Trades channel"
                );
            this.consolidateTaker = consolidateTaker;
            return this;
        }
    }
}
