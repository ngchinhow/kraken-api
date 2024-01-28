package io.github.ngchinhow.kraken.rest.model.userdata.openorders;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter(value = AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractOrder {
    @JsonProperty("refid")
    private String refId;
    @JsonProperty("userref")
    private String userRef;
    private Status status;
    @JsonProperty("opentm")
    private ZonedDateTime openTimestamp;
    @JsonProperty("starttm")
    private ZonedDateTime startTimestamp;
    @JsonProperty("expiretm")
    private ZonedDateTime expireTimestamp;
    @JsonProperty("descr")
    private Description description;
    @JsonProperty("vol")
    private BigDecimal volume;
    @JsonProperty("vol_exec")
    private BigDecimal volumeExecuted;
    private BigDecimal cost;
    private BigDecimal fee;
    private BigDecimal price;
    @JsonProperty("stopprice")
    private BigDecimal stopPrice;
    @JsonProperty("limitprice")
    private BigDecimal limitPrice;
    private Trigger trigger;
    @JsonProperty("misc")
    private List<MiscellaneousInformation> miscellaneousInformationList;
    private List<Flag> flagList;
    private List<String> trades;

    @Getter
    @Setter(value = AccessLevel.PACKAGE)
    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Description {
        private String pair;
        @JsonProperty("type")
        private Side side;
        @JsonProperty("ordertype")
        private OrderType orderType;
        private BigDecimal price;
        private BigDecimal price2;
        private BigDecimal leverage;
        @JsonProperty("order")
        private String orderDescription;
        @JsonProperty("close")
        private String closeDescription;

        @Getter(onMethod_ = @JsonValue)
        @RequiredArgsConstructor
        public enum Side {
            BUY("buy"),
            SELL("sell");

            private final String side;
        }

        @Getter(onMethod_ = @JsonValue)
        @RequiredArgsConstructor
        public enum OrderType {
            MARKET("market"),
            LIMIT("limit"),
            STOP_LOSS("stop-loss"),
            TAKE_PROFIT("take-profit"),
            STOP_LOSS_LIMIT("stop-loss-limit"),
            TAKE_PROFIT_LIMIT("take-profit-limit"),
            SETTLE_POSITION("settle-position");

            private final String orderType;
        }
    }

    @Getter(onMethod_ = @JsonValue)
    @RequiredArgsConstructor
    public enum Status {
        PENDING("pending"),
        OPEN("open"),
        CLOSED("closed"),
        CANCELED("canceled"),
        EXPIRED("expired");

        private final String status;
    }

    @Getter(onMethod_ = @JsonValue)
    @RequiredArgsConstructor
    public enum Trigger {
        INDEX("index"),
        LAST("last");

        private final String trigger;
    }

    @Getter(onMethod_ = @JsonValue)
    @RequiredArgsConstructor
    public enum MiscellaneousInformation {
        STOPPED("stopped"),
        TOUCHED("touched"),
        LIQUIDATED("liquidated"),
        PARTIAL("partial");

        private final String miscellaneousInfo;
    }

    @Getter(onMethod_ = @JsonValue)
    @RequiredArgsConstructor
    public enum Flag {
        POST("post"),
        FCIB("fcib"),
        FCIQ("fciq"),
        NOMPP("nompp"),
        VIQC("viqc");

        private final String flag;
    }
}
