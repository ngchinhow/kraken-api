package io.github.ngchinhow.kraken.rest.model.marketdata.book;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.ngchinhow.kraken.rest.model.ResultInterface;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public final class OrderBookResult implements ResultInterface {
    private String assetPairName;
    private OrderBook orderBook;

    @JsonAnySetter
    public void setOrderBookMap(String assetPairName, OrderBook orderBook) {
        this.assetPairName = assetPairName;
        this.orderBook = orderBook;
    }

    @Data
    public static class OrderBook {
        private List<PriceVolume> asks;
        private List<PriceVolume> bids;

        @Data
        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        @JsonPropertyOrder({"price", "volume", "timestamp"})
        public static class PriceVolume {
            private BigDecimal price;
            private BigDecimal volume;
            private int timestamp;
        }
    }
}
