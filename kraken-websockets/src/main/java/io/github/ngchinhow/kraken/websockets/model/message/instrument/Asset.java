package io.github.ngchinhow.kraken.websockets.model.message.instrument;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.*;

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

    @Getter(onMethod_ = @JsonValue)
    @RequiredArgsConstructor
    public enum Status {
        ENABLED("enabled"),
        DEPOSIT_ONLY("depositonly"),
        WITHDRAWAL_ONLY("withdrawalonly"),
        FUNDING_TEMPORARILY_DISABLED("fundingtemporarilydisabled"),
        DISABLED("disabled"),
        WORK_IN_PROGRESS("workinprogress");

        private final String status;
    }
}