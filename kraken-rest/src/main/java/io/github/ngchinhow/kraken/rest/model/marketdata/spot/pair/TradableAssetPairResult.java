package io.github.ngchinhow.kraken.rest.model.marketdata.spot.pair;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.ngchinhow.kraken.common.enumerations.BaseKrakenEnum;
import io.github.ngchinhow.kraken.rest.model.ResultInterface;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TradableAssetPairResult implements ResultInterface {
    private Map<String, AssetPair> assetPairs = new HashMap<>();

    @SuppressWarnings("unused")
    @JsonAnySetter
    public void addAssetPair(String assetPairName, AssetPair assetPair) {
        assetPair.setName(assetPairName);
        assetPairs.put(assetPairName, assetPair);
    }

    @Data
    public static class AssetPair {
        private String name;
        @JsonProperty("altname")
        private String alternateName;
        @JsonProperty("wsname")
        private String webSocketName;
        @JsonProperty("aclass_base")
        private String baseAssetClass;
        private String base;
        @JsonProperty("aclass_quote")
        private String quoteAssetClass;
        private String quote;
        private String lot;
        @JsonProperty("pair_decimals")
        private int pairDecimals;
        @JsonProperty("cost_decimals")
        private int costDecimals;
        @JsonProperty("lot_decimals")
        private int lotDecimals;
        @JsonProperty("lot_multiplier")
        private int lotMultiplier;
        @JsonProperty("leverage_buy")
        private List<Integer> leverageBuy;
        @JsonProperty("leverage_sell")
        private List<Integer> leverageSell;
        @JsonProperty("fees")
        private List<Fee> takerFees;
        @JsonProperty("fees_maker")
        private List<Fee> makerFees;
        @JsonProperty("fee_volume_currency")
        private String feeVolumeCurrency;
        @JsonProperty("margin_call")
        private int marginCall;
        @JsonProperty("margin_stop")
        private int marginStop;
        @JsonProperty("ordermin")
        private BigDecimal minOrder;
        @JsonProperty("costmin")
        private BigDecimal minCost;
        @JsonProperty("tick_size")
        private BigDecimal tickSize;
        private Status status;
        @JsonProperty("long_position_limit")
        private int longPositionLimit;
        @JsonProperty("short_position_limit")
        private int shortPositionLimit;

        public enum Status implements BaseKrakenEnum {
            ONLINE,
            CANCEL_ONLY,
            POST_ONLY,
            LIMIT_ONLY,
            REDUCE_ONLY
        }

        @Data
        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        @JsonPropertyOrder({"minVolume", "percentFee"})
        public static class Fee {
            private BigInteger minVolume;
            private BigDecimal percentFee;
        }
    }
}