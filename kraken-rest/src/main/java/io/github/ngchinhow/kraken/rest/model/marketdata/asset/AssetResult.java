package io.github.ngchinhow.kraken.rest.model.marketdata.asset;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.common.enumerations.BaseKrakenEnum;
import io.github.ngchinhow.kraken.rest.model.ResultInterface;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public final class AssetResult implements ResultInterface {
    private Map<String, Asset> assets = new HashMap<>();

    @JsonAnySetter
    public void addAsset(String assetName, Asset asset) {
        asset.setName(assetName);
        assets.put(assetName, asset);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Asset {
        private String name;
        @JsonProperty("aclass")
        private String assetClass;
        @JsonProperty("altname")
        private String alternateName;
        private int decimals;
        @JsonProperty("display_decimals")
        private int displayDecimals;
        @JsonProperty("collateral_value")
        private BigDecimal collateralValue;
        private Status status;

        public enum Status implements BaseKrakenEnum {
            ENABLED,
            DEPOSIT_ONLY,
            WITHDRAWAL_ONLY,
            FUNDING_TEMPORARILY_DISABLED
        }
    }
}