package io.github.ngchinhow.kraken.rest.model.marketdata;

import com.fasterxml.jackson.annotation.*;
import feign.Param;
import io.github.ngchinhow.kraken.rest.model.AbstractRequest;
import io.github.ngchinhow.kraken.rest.model.AbstractResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TradableAssetPairs {

    @Data
    @SuperBuilder
    @NoArgsConstructor
    public static class Request implements AbstractRequest {
        @Param("pair")
        private List<String> pairs;
        private Info info;

        @SuppressWarnings("unused")
        private enum Info {
            INFO("info"),
            LEVERAGE("leverage"),
            FEES("fees"),
            MARGIN("margin");

            private final String info;

            Info(String info) {
                this.info = info;
            }

            @JsonValue
            public String getInfo() {
                return this.info;
            }
        }
    }

    @Data
    public static class Result implements AbstractResult {
        private Map<String, AssetPair> assetPairs = new HashMap<>();

        @SuppressWarnings("unused")
        @JsonAnySetter
        public void setAssetPairs(String assetPairName, AssetPair assetPair) {
            assetPairs.put(assetPairName, assetPair);
        }

        @Data
        public static class AssetPair {
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

            @SuppressWarnings("unused")
            private enum Status {
                ONLINE("online"),
                CANCEL_ONLY("cancel_only"),
                POST_ONLY("post_only"),
                LIMIT_ONLY("limit_only"),
                REDUCE_ONLY("reduce_only");

                private final String status;

                Status(String status) {
                    this.status = status;
                }

                @JsonValue
                public String getStatus() {
                    return this.status;
                }
            }

            @Data
            @JsonFormat(shape = JsonFormat.Shape.ARRAY)
            @JsonPropertyOrder({"minVolume", "percentFee"})
            public static class Fee {
                private int minVolume;
                private int percentFee;
            }
        }
    }
}
