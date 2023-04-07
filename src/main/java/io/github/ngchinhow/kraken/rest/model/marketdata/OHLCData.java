package io.github.ngchinhow.kraken.rest.model.marketdata;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.ngchinhow.kraken.rest.model.AbstractRequest;
import io.github.ngchinhow.kraken.rest.model.AbstractResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

public abstract class OHLCData {

    @Data
    @SuperBuilder
    @NoArgsConstructor(force = true)
    public static class Request implements AbstractRequest {
        @NonNull
        private String pair;
        private Integer interval;
        private Long since;
    }

    @Data
    public static class Result implements AbstractResult {
        private Long last;
        private String assetPairName;
        private List<Tick> ticks;

        @SuppressWarnings("unused")
        @JsonAnySetter
        public void parseOHLCData(String assetPairName, List<Tick> ticks) {
            this.assetPairName = assetPairName;
            this.ticks = ticks;
        }

        @Data
        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        @JsonPropertyOrder({"time", "open", "high", "low", "close", "vwap", "volume", "count"})
        public static class Tick {
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
}
