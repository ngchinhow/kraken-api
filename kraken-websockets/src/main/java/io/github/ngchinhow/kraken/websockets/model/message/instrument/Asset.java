package io.github.ngchinhow.kraken.websockets.model.message.instrument;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor(force = true)
public class Asset {

    @NonNull
    private Boolean borrowable;
    @JsonProperty(value = "collateral_value", required = true)
    private BigDecimal collateralValue;
    @NonNull
    private String id;
    @JsonProperty("margin_rate")
    private BigDecimal marginRate;
    @NonNull
    private Integer precision;
    @JsonProperty(value = "precision_display", required = true)
    private Integer precisionDisplay;
    @NonNull
    private Status status;

    public enum Status {
        ENABLED,
        DEPOSIT_ONLY,
        WITHDRAWAL_ONLY,
        FUNDING_TEMPORARILY_DISABLED,
        DISABLED,
        WORK_IN_PROGRESS;

        @JsonValue
        public String toJsonString() {
            return this.name().toLowerCase().replace("_", "");
        }
    }
}