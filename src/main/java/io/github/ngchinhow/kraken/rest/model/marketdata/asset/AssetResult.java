package io.github.ngchinhow.kraken.rest.model.marketdata.asset;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.github.ngchinhow.kraken.rest.model.AbstractResult;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AssetResult implements AbstractResult {
    private Map<String, RestAsset> assets = new HashMap<>();

    @JsonAnySetter
    public void addAsset(String assetName, RestAsset asset) {
        assets.put(assetName, asset);
    }
}