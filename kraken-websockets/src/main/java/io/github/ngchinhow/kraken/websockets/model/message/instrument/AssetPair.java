package io.github.ngchinhow.kraken.websockets.model.message.instrument;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.common.enumerations.BaseKrakenEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor(force = true)
public class AssetPair {
    @NonNull
    private String base;
    @JsonProperty(value = "cost_min", required = true)
    private BigDecimal minCost;
    @JsonProperty(value = "cost_precision", required = true)
    private int costPrecision;
    @JsonProperty(value = "has_index", required = true)
    private boolean hasIndex;
    @JsonProperty("margin_initial")
    private BigDecimal initialMargin;
    @NonNull
    private Boolean marginable;
    @JsonProperty("position_limit_long")
    private Integer positionLimitLong;
    @JsonProperty("position_limit_short")
    private Integer positionLimitShort;
    @JsonProperty(value = "price_increment", required = true)
    private BigDecimal priceIncrement;
    @JsonProperty(value = "price_precision", required = true)
    private Integer pricePrecision;
    @JsonProperty(value = "qty_increment", required = true)
    private BigDecimal quantityIncrement;
    @JsonProperty(value = "qty_min", required = true)
    private BigDecimal minQuantity;
    @JsonProperty(value = "qty_precision", required = true)
    private int quantityPrecision;
    @NonNull
    private String quote;
    @NonNull
    private Status status;
    @NonNull
    private String symbol;
    @JsonProperty("tick_size")
    private BigDecimal tickSize;

    public enum Status implements BaseKrakenEnum {
        CANCEL_ONLY,
        DELISTED,
        LIMIT_ONLY,
        MAINTENANCE,
        ONLINE,
        POST_ONLY,
        REDUCE_ONLY,
        WORK_IN_PROGRESS
    }
}
