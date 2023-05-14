package io.github.ngchinhow.kraken.websocket.model.message.executions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;
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
    private Boolean hasNotMarketPriceProtection;
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
    private Trigger trigger;
    @JsonProperty("triggered_price")
    private BigDecimal triggeredPrice;

    @Getter(onMethod = @__(@JsonValue))
    @RequiredArgsConstructor
    public enum ExecutionType {
        PENDING_NEW("pending_new"),
        NEW("new"),
        FILLED("filled"),
        CANCELED("canceled"),
        EXPIRED("expired"),
        TRADE("trade");

        private final String executionType;
    }

    @Getter(onMethod = @__(@JsonValue))
    @RequiredArgsConstructor
    public enum FeeCurrencyPreference {
        FCIB("fcib"),
        FCIQ("fciq");

        private final String feePreference;
    }

    @Getter(onMethod = @__(@JsonValue))
    @RequiredArgsConstructor
    public enum LiquidityIndicator {
        TAKER("t"),
        MAKER("m");

        private final String liquidityIndicator;
    }

    @Getter(onMethod = @__(@JsonValue))
    @RequiredArgsConstructor
    public enum OrderStatus {
        PENDING_NEW("pending_new"),
        NEW("new"),
        FILLED("filled"),
        CANCELED("canceled"),
        EXPIRED("expired"),
        TRIGGERED("triggered"),
        PARTIALLY_FILLED("partially_filled");

        private final String orderStatus;
    }

    @Getter(onMethod = @__(@JsonValue))
    @RequiredArgsConstructor
    public enum OrderType {
        MARKET("market"),
        LIMIT("limit"),
        STOP_LOSS("stop-loss"),
        TAKE_PROFIT("take-profit"),
        STOP_LOSS_PROFIT("stop-loss-profit"),
        STOP_LOSS_PROFIT_LIMIT("stop-loss-profit-limit"),
        STOP_LOSS_LIMIT("stop-loss-limit"),
        TAKE_PROFIT_LIMIT("take-profit-limit"),
        TRAILING_STOP("trailing-stop"),
        TRAILING_STOP_LIMIT("trailing-stop-limit"),
        STOP_LOSS_AND_LIMIT("stop-loss-and-limit"),
        SETTLE_POSITION("settle-position"),
        PEG_BID("peg-bid"),
        PEG_ASK("peg-ask");

        private final String orderType;
    }

    @Getter(onMethod = @__(@JsonValue))
    @RequiredArgsConstructor
    public enum Side {
        BUY("buy"),
        SELL("sell");

        private final String side;
    }

    @Getter
    public enum TimeInForce {
        IOC,
        GTD,
        GTC
    }

    @Getter(onMethod = @__(@JsonValue))
    @RequiredArgsConstructor
    public enum Trigger {
        INDEX("index");

        private final String trigger;
    }
}