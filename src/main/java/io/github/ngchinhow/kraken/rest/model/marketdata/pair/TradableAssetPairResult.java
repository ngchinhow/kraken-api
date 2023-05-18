package io.github.ngchinhow.kraken.rest.model.marketdata.pair;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.github.ngchinhow.kraken.rest.model.AbstractResult;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TradableAssetPairResult implements AbstractResult {
    private Map<String, RestAssetPair> assetPairs = new HashMap<>();

    @SuppressWarnings("unused")
    @JsonAnySetter
    public void setAssetPairs(String assetPairName, RestAssetPair assetPair) {
        assetPairs.put(assetPairName, assetPair);
    }
}