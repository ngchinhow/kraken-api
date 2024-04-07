package io.github.ngchinhow.kraken.rest.model.marketdata.spot.ohlc;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.ngchinhow.kraken.rest.model.ResultInterface;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OHLCResult implements ResultInterface {
    private Long last;
    private String assetPairName;
    private List<OHLC> ohlcList;

    @SuppressWarnings("unused")
    @JsonAnySetter
    public void setOHLCData(String assetPairName, List<OHLC> ohlcList) {
        this.assetPairName = assetPairName;
        this.ohlcList = ohlcList;
    }

    @Data
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    @JsonPropertyOrder({"time", "open", "high", "low", "close", "vwap", "volume", "count"})
    public static class OHLC {
        private Long time;
        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal close;
        private BigDecimal vwap;
        private BigDecimal volume;
        private int count;
    }
}