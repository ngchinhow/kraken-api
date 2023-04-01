package com.kraken.api.javawrapper.websocket.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import com.kraken.api.javawrapper.websocket.enums.ChannelMetadata;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class TickerMessage extends AbstractPublicationMessage {
    private List<Ticker> data;

    {
        this.setChannel(ChannelMetadata.ChannelType.TICKER);
    }

    @Override
    public RequestIdentifier toRequestIdentifier() {
        return super.toRequestIdentifier().toBuilder()
            .symbol(data.get(0).symbol)
            .build();
    }

    @Data
    @NoArgsConstructor(force = true)
    public static class Ticker {
        @NonNull
        private BigDecimal ask;
        @JsonProperty(value = "ask_qty", required = true)
        private BigDecimal askQuantity;
        @NonNull
        private BigDecimal bid;
        @JsonProperty(value = "bid_qty", required = true)
        private BigDecimal bidQuantity;
        @NonNull
        private BigDecimal change;
        @JsonProperty(value = "change_pct", required = true)
        private BigDecimal changeInPercentage;
        @NonNull
        private BigDecimal high;
        @NonNull
        private BigDecimal last;
        @NonNull
        private BigDecimal low;
        @NonNull
        private String symbol;
        @NonNull
        private BigDecimal volume;
        @NonNull
        private BigDecimal vwap;
    }
}
