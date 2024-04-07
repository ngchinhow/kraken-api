package io.github.ngchinhow.kraken.rest.model.marketdata.spot.spreads;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.ngchinhow.kraken.rest.model.ResultInterface;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class RecentSpreadsResult implements ResultInterface {
    private String assetPairName;
    private List<SpreadData> spreadData;
    private String last;

    @JsonAnySetter
    public void setSpreadData(String assetPairName, List<SpreadData> spreadData) {
        this.assetPairName = assetPairName;
        this.spreadData = spreadData;
    }

    @Data
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    @JsonPropertyOrder({"time", "bid", "ask"})
    public static class SpreadData {
        private ZonedDateTime time;
        private BigDecimal bid;
        private BigDecimal ask;
    }
}
