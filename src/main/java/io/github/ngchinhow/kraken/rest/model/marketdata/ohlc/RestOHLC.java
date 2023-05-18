package io.github.ngchinhow.kraken.rest.model.marketdata.ohlc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"time", "open", "high", "low", "close", "vwap", "volume", "count"})
public class RestOHLC {
    private Long time;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal vwap;
    private BigDecimal volume;
    private int count;
}