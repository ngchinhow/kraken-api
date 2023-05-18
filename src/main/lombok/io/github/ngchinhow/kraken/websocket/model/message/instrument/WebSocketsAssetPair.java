package io.github.ngchinhow.kraken.websocket.model.message.instrument;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@NoArgsConstructor(force = true)
public class WebSocketsAssetPair {
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

    @Getter(onMethod = @__(@JsonValue))
    @RequiredArgsConstructor
    public enum Status {
        CANCEL_ONLY("cancel_only"),
        DELISTED("delisted"),
        LIMIT_ONLY("limit_only"),
        MAINTENANCE("maintenance"),
        ONLINE("online"),
        POST_ONLY("post_only"),
        REDUCE_ONLY("reduce_only"),
        WORK_IN_PROGRESS("work_in_progress");

        private final String status;
    }
}
