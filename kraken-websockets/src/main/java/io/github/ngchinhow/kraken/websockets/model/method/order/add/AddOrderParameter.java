package io.github.ngchinhow.kraken.websockets.model.method.order.add;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.github.ngchinhow.kraken.common.enumerations.BaseKrakenEnum;
import io.github.ngchinhow.kraken.common.enumerations.Side;
import io.github.ngchinhow.kraken.common.enumerations.TimeInForce;
import io.github.ngchinhow.kraken.common.enumerations.TriggerReference;
import io.github.ngchinhow.kraken.websockets.model.method.order.AbstractOrderParameter;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public final class AddOrderParameter extends AbstractOrderParameter {
    @JsonProperty(value = "cash_order_qty")
    private Integer cashOrderQuantity;
    private Conditional conditional;
    private ZonedDateTime deadline;
    @JsonProperty(value = "display_qty")
    private BigDecimal displayQuantity;
    @JsonProperty(value = "effective_time")
    private ZonedDateTime effectiveTime;
    @JsonProperty(value = "expire_time")
    private ZonedDateTime expireTime;
    @JsonProperty(value = "fee_preference")
    private FeePreference feePreference;
    @JsonProperty(value = "limit_price")
    private BigDecimal limitPrice;
    @JsonProperty(value = "limit_price_type")
    private OrderPriceType limitPriceType;
    private Boolean margin;
    @JsonProperty(value = "no_mpp")
    private Boolean hasNoMarketPriceProtection;
    @JsonProperty(value = "order_type", required = true)
    private OrderType orderType;
    @JsonProperty(value = "order_userref")
    private BigInteger orderUserReference;
    @JsonProperty(value = "post_only")
    private Boolean postOnly;
    @JsonProperty(value = "reduce_only")
    private Boolean reduceOnly;
    @JsonProperty(required = true)
    private Side side;
    @JsonProperty(value = "stp_type")
    private SelfTradeType selfTradeType;
    @JsonProperty(required = true)
    private String symbol;
    @JsonProperty(value = "time_in_force")
    private TimeInForce timeInForce;
    private Boolean validate;
    private Triggers triggers;

    @Data
    @Builder
    public static class Conditional {
        @JsonProperty(value = "order_type", required = true)
        private OrderType orderType;
        @JsonProperty(value = "limit_price")
        private BigDecimal limitPrice;
        @JsonProperty(value = "limit_price_type")
        private OrderPriceType limitPriceType;
        @JsonProperty(value = "trigger_price", required = true)
        private BigDecimal triggerPrice;
        @JsonProperty(value = "trigger_price_type")
        private OrderPriceType triggerPriceType;
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

    public enum FeePreference implements BaseKrakenEnum {
        BASE,
        QUOTE
    }

    public enum OrderType implements BaseKrakenEnum {
        LIMIT,
        MARKET,
        ICEBERG,
        STOP_LOSS,
        STOP_LOSS_LIMIT,
        TAKE_PROFIT,
        TAKE_PROFIT_LIMIT,
        TRAILING_STOP,
        TRAILING_STOP_LIMIT,
        SETTLE_POSITION
    }

    public enum SelfTradeType {
        CANCEL_BOTH,
        CANCEL_NEWEST,
        CANCEL_OLDEST;

        @JsonValue
        public String toJsonString() {
            return this.name().toLowerCase();
        }
    }
}
