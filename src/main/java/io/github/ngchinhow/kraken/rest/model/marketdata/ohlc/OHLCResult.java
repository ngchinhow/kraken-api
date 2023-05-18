package io.github.ngchinhow.kraken.rest.model.marketdata.ohlc;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.github.ngchinhow.kraken.rest.model.AbstractResult;
import lombok.Data;

import java.util.List;

@Data
public class OHLCResult implements AbstractResult {
    private Long last;
    private String assetPairName;
    private List<RestOHLC> ohlc;

    @SuppressWarnings("unused")
    @JsonAnySetter
    public void setOHLCData(String assetPairName, List<RestOHLC> ohlc) {
        this.assetPairName = assetPairName;
        this.ohlc = ohlc;
    }
}