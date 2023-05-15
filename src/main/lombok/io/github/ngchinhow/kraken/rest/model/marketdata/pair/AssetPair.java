package io.github.ngchinhow.kraken.rest.model.marketdata.pair;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AssetPair {
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

    @Getter(onMethod = @__(@JsonValue))
    @RequiredArgsConstructor
    public enum Status {
        ONLINE("online"),
        CANCEL_ONLY("cancel_only"),
        POST_ONLY("post_only"),
        LIMIT_ONLY("limit_only"),
        REDUCE_ONLY("reduce_only");

        private final String status;
    }
}