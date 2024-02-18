package io.github.ngchinhow.kraken.websockets.model.method.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.github.ngchinhow.kraken.common.enumerations.BaseKrakenEnum;
import io.github.ngchinhow.kraken.common.enumerations.Side;
import io.github.ngchinhow.kraken.common.enumerations.TimeInForce;
import io.github.ngchinhow.kraken.websockets.model.method.order.add.AddOrderParameter;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseOrderCreationInput extends BaseOrderInput {
    @JsonProperty(value = "cash_order_qty")
    private Integer cashOrderQuantity;
    private Conditional conditional;
    @JsonProperty(value = "effective_time")
    private ZonedDateTime effectiveTime;
    @JsonProperty(value = "expire_time")
    private ZonedDateTime expireTime;
    private Boolean margin;
    @JsonProperty(value = "order_type", required = true)
    private OrderType orderType;
    @JsonProperty(required = true)
    private Side side;
    @JsonProperty(value = "stp_type")
    private SelfTradeType selfTradeType;
    @JsonProperty(value = "time_in_force")
    private TimeInForce timeInForce;

    @Data
    @Builder
    public static class Conditional {
        @JsonProperty(value = "order_type", required = true)
        private OrderType orderType;
        @JsonProperty(value = "limit_price")
        private BigDecimal limitPrice;
        @JsonProperty(value = "limit_price_type")
        private AddOrderParameter.OrderPriceType limitPriceType;
        @JsonProperty(value = "trigger_price", required = true)
        private BigDecimal triggerPrice;
        @JsonProperty(value = "trigger_price_type")
        private AddOrderParameter.OrderPriceType triggerPriceType;
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
