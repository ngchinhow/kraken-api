package io.github.ngchinhow.kraken.websockets.model.message.ticker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor(force = true)
public class Ticker {
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