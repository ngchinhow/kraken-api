package io.github.ngchinhow.kraken.rest.model.marketdata.asset;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter(value = AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RestAsset {
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

    @Getter(onMethod_ = @JsonValue)
    @RequiredArgsConstructor
    public enum Status {
        ENABLED("enabled"),
        DEPOSIT_ONLY("deposit_only"),
        WITHDRAWAL_ONLY("withdrawal_only"),
        FUNDING_TEMPORARILY_DISABLED("funding_temporarily_disabled");

        private final String status;
    }
}