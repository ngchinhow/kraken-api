package io.github.ngchinhow.kraken.rest.model.marketdata.trades;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import io.github.ngchinhow.kraken.rest.model.AbstractResult;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public final class RecentTradesResult implements AbstractResult {
    private String assetPairName;
    private List<TickData> tickData;
    private String last;

    @JsonAnySetter
    public void setTickData(String assetPairName, List<TickData> tickDataList) {
        this.assetPairName = assetPairName;
        this.tickData = tickDataList;
    }

    @Data
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    @JsonPropertyOrder({"price", "volume", "time", "side", "orderType", "miscellaneous", "tradeId"})
    public static class TickData {
        private BigDecimal price;
        private BigDecimal volume;
        private ZonedDateTime time;
        private Side side;
        private OrderType orderType;
        private String miscellaneous;
        private int tradeId;

        public enum Side {
            BUY,
            SELL;

            @JsonValue
            public String getSide() {
                return this.name().substring(0, 1).toLowerCase();
            }
        }

        public enum OrderType {
            MARKET,
            LIMIT;

            @JsonValue
            public String getOrderType() {
                return this.name().substring(0, 1).toLowerCase();
            }
        }
    }
}
