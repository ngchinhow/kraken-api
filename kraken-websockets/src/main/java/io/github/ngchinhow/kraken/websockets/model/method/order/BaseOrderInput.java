package io.github.ngchinhow.kraken.websockets.model.method.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.common.enumerations.BaseKrakenEnum;
import io.github.ngchinhow.kraken.common.enumerations.TriggerReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseOrderInput {
    @JsonProperty(value = "display_qty")
    private BigDecimal displayQuantity;
    @JsonProperty(value = "fee_preference")
    private FeePreference feePreference;
    @JsonProperty(value = "limit_price")
    private BigDecimal limitPrice;
    @JsonProperty(value = "no_mpp")
    private Boolean hasNoMarketPriceProtection;
    @JsonProperty(value = "order_qty")
    private BigDecimal orderQuantity;
    @JsonProperty(value = "order_userref")
    private BigInteger orderUserReference;
    @JsonProperty(value = "post_only")
    private Boolean postOnly;
    @JsonProperty(value = "reduce_only")
    private Boolean reduceOnly;
    private Triggers triggers;

    public enum FeePreference implements BaseKrakenEnum {
        BASE,
        QUOTE
    }

    @Data
    @Builder
    public static class Triggers {
        private TriggerReference reference;
        @JsonProperty(required = true)
        private BigDecimal price;
        private OrderPriceType priceType;
    }

    public enum OrderPriceType implements BaseKrakenEnum {
        STATIC,
        PCT,
        QUOTE
    }
}
