package io.github.ngchinhow.kraken.websocket.model.message.ohlc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor(force = true)
public class WebSocketsOHLC {
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