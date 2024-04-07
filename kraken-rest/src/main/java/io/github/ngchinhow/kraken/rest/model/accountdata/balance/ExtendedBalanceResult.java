package io.github.ngchinhow.kraken.rest.model.accountdata.balance;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.rest.model.ResultInterface;
import lombok.Value;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Value
public class ExtendedBalanceResult implements ResultInterface {
    Map<String, ExtendedBalance> assets = new HashMap<>();

    @JsonAnySetter
    public void setMap(String assetName, ExtendedBalance balance) {
        this.assets.put(assetName, balance);
    }

    public record ExtendedBalance(BigDecimal balance,
                                  BigDecimal credit,
                                  @JsonProperty("credit_used") BigDecimal creditUsed,
                                  @JsonProperty("hold_trade") BigDecimal holdTrade) {
    }
}
