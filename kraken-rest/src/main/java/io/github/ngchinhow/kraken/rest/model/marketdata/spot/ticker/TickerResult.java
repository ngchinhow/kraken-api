package io.github.ngchinhow.kraken.rest.model.marketdata.spot.ticker;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.ngchinhow.kraken.rest.model.ResultInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public final class TickerResult implements ResultInterface {
    private final Map<String, AssetTickerInfo> assetTickerInfoMap = new HashMap<>();

    @SuppressWarnings("unused")
    @JsonAnySetter
    public void setAssetTickerInfoMap(String assetPairName, AssetTickerInfo assetTickerInfo) {
        assetTickerInfoMap.put(assetPairName, assetTickerInfo);
    }

    @Data
    public static class AssetTickerInfo {
        @JsonProperty("a")
        private PriceWholeVolume ask;
        @JsonProperty("b")
        private PriceWholeVolume bid;
        @JsonProperty("c")
        private PriceVolume close;
        @JsonProperty("v")
        private TodayLast24Hours volume;
        @JsonProperty("p")
        private TodayLast24Hours vwap;
        @JsonProperty("t")
        private TodayLast24Hours numberOfTrades;
        @JsonProperty("l")
        private TodayLast24Hours low;
        @JsonProperty("h")
        private TodayLast24Hours high;
        @JsonProperty("o")
        private BigDecimal open;

        @Data
        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        @JsonPropertyOrder({"price", "lotVolume"})
        public static class PriceVolume {
            private BigDecimal price;
            private BigDecimal lotVolume;
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        @JsonPropertyOrder({"price", "wholeLotVolume", "lotVolume"})
        public static class PriceWholeVolume extends PriceVolume {
            private BigDecimal wholeLotVolume;
        }

        @Data
        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        @JsonPropertyOrder({"today", "last24Hours"})
        public static class TodayLast24Hours {
            private BigDecimal today;
            private BigDecimal last24Hours;
        }
    }
}
