package io.github.ngchinhow.kraken.websockets.model.message.executions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.github.ngchinhow.kraken.common.enumerations.BaseKrakenEnum;
import io.github.ngchinhow.kraken.common.enumerations.Side;
import io.github.ngchinhow.kraken.common.enumerations.TimeInForce;
import io.github.ngchinhow.kraken.common.enumerations.TriggerReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Execution {
    @JsonProperty("avg_price")
    private BigDecimal averagePrice;
    @JsonProperty("cancel_reason")
    private String cancelReason;
    private BigDecimal cost;
    @JsonProperty("cum_cost")
    private BigDecimal cumulativeCost;
    @JsonProperty("cum_qty")
    private BigDecimal cumulativeQuantity;
    @JsonProperty("display_qty")
    private BigDecimal displayQuantity;
    @JsonProperty("display_qty_remain")
    private BigDecimal displayQuantityRemaining;
    @JsonProperty("effective_time")
    private ZonedDateTime effectiveTime;
    @JsonProperty("exec_id")
    private String executionId;
    @JsonProperty(value = "exec_type", required = true)
    private ExecutionType executionType;
    @JsonProperty("expire_time")
    private ZonedDateTime expireTime;
    @JsonProperty("fee_ccy_pref")
    private FeeCurrencyPreference feeCurrencyPreference;
    @JsonProperty("fee_usd_equiv")
    private BigDecimal feeUSDEquivalent;
    private List<Fee> fees;
    @JsonProperty("last_price")
    private BigDecimal lastPrice;
    @JsonProperty("last_qty")
    private BigDecimal lastQuantity;
    @JsonProperty("limit_price")
    private BigDecimal limitPrice;
    @JsonProperty("liquidity_ind")
    private LiquidityIndicator liquidityIndicator;
    private Boolean margin;
    @JsonProperty("no_mpp")
    private Boolean hasNoMarketPriceProtection;
    @JsonProperty(value = "ord_ref_id", required = true)
    private String referralOrderId;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("order_qty")
    private BigDecimal orderQuantity;
    @JsonProperty(value = "order_status", required = true)
    private OrderStatus orderStatus;
    @JsonProperty("order_type")
    private OrderType orderType;
    @JsonProperty("order_userref")
    private Integer orderUserReference;
    @JsonProperty("post_only")
    private Boolean postOnly;
    @JsonProperty("reduce_only")
    private Boolean reduceOnly;
    private Side side;
    @JsonProperty("stop_price")
    private BigDecimal stopPrice;
    private String symbol;
    @JsonProperty("time_in_force")
    private TimeInForce timeInForce;
    private ZonedDateTime timestamp;
    @JsonProperty("trade_id")
    private Integer tradeId;
    private TriggerReference trigger;
    @JsonProperty("triggered_price")
    private BigDecimal triggeredPrice;

    public enum ExecutionType implements BaseKrakenEnum {
        PENDING_NEW,
        NEW,
        FILLED,
        CANCELED,
        EXPIRED,
        TRADE
    }

    public enum FeeCurrencyPreference implements BaseKrakenEnum {
        FCIB,
        FCIQ
    }

    @RequiredArgsConstructor
    public enum LiquidityIndicator {
        TAKER("t"),
        MAKER("m");

        private final String liquidityIndicator;

        @JsonValue
        public String getLiquidityIndicator() {
            return liquidityIndicator;
        }
    }

    public enum OrderStatus implements BaseKrakenEnum {
        PENDING_NEW,
        NEW,
        FILLED,
        CANCELED,
        EXPIRED,
        TRIGGERED,
        PARTIALLY_FILLED
    }

    public enum OrderType implements BaseKrakenEnum {
        MARKET,
        LIMIT,
        STOP_LOSS,
        TAKE_PROFIT,
        STOP_LOSS_PROFIT,
        STOP_LOSS_PROFIT_LIMIT,
        STOP_LOSS_LIMIT,
        TAKE_PROFIT_LIMIT,
        TRAILING_STOP,
        TRAILING_STOP_LIMIT,
        STOP_LOSS_AND_LIMIT,
        SETTLE_POSITION,
        PEG_BID,
        PEG_ASK
    }
}