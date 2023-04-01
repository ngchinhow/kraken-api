package com.kraken.api.javawrapper.websocket.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import com.kraken.api.javawrapper.websocket.enums.ChannelMetadata;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class OHLCMessage extends AbstractPublicationMessage {
    private List<OHLC> data;
    private ZonedDateTime timestamp;

    {
        this.setChannel(ChannelMetadata.ChannelType.OHLC);
    }

    @Override
    public RequestIdentifier toRequestIdentifier() {
        return super.toRequestIdentifier().toBuilder()
            .symbol(data.get(0).symbol)
            .build();
    }

    @Data
    @NoArgsConstructor(force = true)
    public static class OHLC {
        @NonNull
        private BigDecimal close;
        @NonNull
        private BigDecimal high;
        @NonNull
        private BigDecimal low;
        @NonNull
        private BigDecimal open;
        @NonNull
        private String symbol;
        @JsonProperty(value = "interval_begin", required = true)
        private ZonedDateTime intervalBegin;
        @NonNull
        private Integer trades;
        @NonNull
        private BigDecimal volume;
        @NonNull
        private BigDecimal vwap;
        @NonNull
        private BigDecimal interval;
        @NonNull
        private ZonedDateTime timestamp;
    }
}
