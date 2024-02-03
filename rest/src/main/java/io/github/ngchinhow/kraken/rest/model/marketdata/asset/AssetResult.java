package io.github.ngchinhow.kraken.rest.model.marketdata.asset;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.github.ngchinhow.kraken.rest.model.AbstractResult;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
@Setter(AccessLevel.PACKAGE)
@EqualsAndHashCode
public class AssetResult implements AbstractResult {
    private Map<String, RestAsset> assets = new HashMap<>();

    @JsonAnySetter
    public void addAsset(String assetName, RestAsset asset) {
        asset.setName(assetName);
        assets.put(assetName, asset);
    }
}