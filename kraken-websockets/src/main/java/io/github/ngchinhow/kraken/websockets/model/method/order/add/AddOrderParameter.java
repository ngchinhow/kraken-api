package io.github.ngchinhow.kraken.websockets.model.method.order.add;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.github.ngchinhow.kraken.websockets.model.method.order.AbstractOrderParameter;
import io.github.ngchinhow.kraken.websockets.utils.EnumConverter;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AddOrderParameter extends AbstractOrderParameter {
    @JsonProperty(value = "cash_order_qty")
    private Integer cashOrderQuantity;
    private Conditional conditional;

    @Data
    @Builder
    public static class Conditional {
        @JsonProperty(value = "order_type", required = true)
        private OrderType orderType;
        @JsonProperty(value = "limit_price")
        private BigDecimal limitPrice;
        @JsonProperty(value = "limit_price_type")
        private LimitPriceType limitPriceType;

        enum OrderType implement {
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

        enum LimitPriceType {
            STATIC,
            PCT,
            QUOTE;

            @JsonValue
            public String getLimitPriceType() {
                return this.name().toLowerCase();
            }
        }
    }
}
