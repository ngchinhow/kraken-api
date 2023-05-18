package io.github.ngchinhow.kraken.rest.model.marketdata.asset;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.github.ngchinhow.kraken.rest.model.AbstractResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AssetResult implements AbstractResult {
    // The asset name used as the key to the object is not guaranteed to be the same as the asset name sent in the
    // request. The name-object map has to be flattened into a list until Kraken fixes this bug.
    private List<RestAsset> assets = new ArrayList<>();

    @SuppressWarnings("unused")
    @JsonAnySetter
    public void setAssets(String assetName, RestAsset asset) {
        asset.setName(assetName);
        assets.add(asset);
    }
}