package io.github.ngchinhow.kraken.rest.model.marketdata.asset;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.common.enumerations.BaseKrakenEnum;
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

    public enum Status implements BaseKrakenEnum {
        ENABLED,
        DEPOSIT_ONLY,
        WITHDRAWAL_ONLY,
        FUNDING_TEMPORARILY_DISABLED
    }
}