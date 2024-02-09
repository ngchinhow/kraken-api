package io.github.ngchinhow.kraken.rest.model.userdata.openorders;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.common.enumerations.BaseKrakenEnum;
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

        public enum Side implements BaseKrakenEnum {
            BUY,
            SELL
        }

        public enum OrderType implements BaseKrakenEnum {
            MARKET,
            LIMIT,
            STOP_LOSS,
            TAKE_PROFIT,
            STOP_LOSS_LIMIT,
            TAKE_PROFIT_LIMIT,
            SETTLE_POSITION
        }
    }

    public enum Status implements BaseKrakenEnum {
        PENDING,
        OPEN,
        CLOSED,
        CANCELED,
        EXPIRED
    }

    public enum Trigger implements BaseKrakenEnum {
        INDEX,
        LAST
    }

    public enum MiscellaneousInformation implements BaseKrakenEnum {
        STOPPED,
        TOUCHED,
        LIQUIDATED,
        PARTIAL
    }

    public enum Flag implements BaseKrakenEnum {
        POST,
        FCIB,
        FCIQ,
        NOMPP,
        VIQC
    }
}
