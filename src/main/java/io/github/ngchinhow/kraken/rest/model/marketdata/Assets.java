package io.github.ngchinhow.kraken.rest.model.marketdata;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import feign.Param;
import io.github.ngchinhow.kraken.rest.model.AbstractRequest;
import io.github.ngchinhow.kraken.rest.model.AbstractResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Assets {

    @Data
    @SuperBuilder
    @NoArgsConstructor
    public static class Request implements AbstractRequest {
        @Param("asset")
        private List<String> pairs;
        @Param("aclass")
        private String assetClass;
    }

    @Data
    public static class Result implements AbstractResult {
        private Map<String, Asset> assets = new HashMap<>();

        @SuppressWarnings("unused")
        @JsonAnySetter
        public void setAssets(String assetName, Asset asset) {
            assets.put(assetName, asset);
        }

        @Data
        public static class Asset {
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

            @SuppressWarnings("unused")
            public enum Status {
                ENABLED("enabled"),
                DEPOSIT_ONLY("deposit_only"),
                WITHDRAWAL_ONLY("withdrawal_only"),
                FUNDING_TEMPORARILY_DISABLED("funding_temporarily_disabled");

                private final String status;

                Status(String status) {
                    this.status = status;
                }

                @JsonValue
                public String getStatus() {
                    return status;
                }
            }
        }
    }
}
