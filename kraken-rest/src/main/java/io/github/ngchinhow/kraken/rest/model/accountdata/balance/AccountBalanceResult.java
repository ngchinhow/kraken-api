package io.github.ngchinhow.kraken.rest.model.accountdata.balance;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.github.ngchinhow.kraken.rest.model.ResultInterface;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class AccountBalanceResult implements ResultInterface {
    private Map<String, BigDecimal> assets = new HashMap<>();

    @JsonAnySetter
    public void addAsset(String assetName, BigDecimal amount) {
        assets.put(assetName, amount);
    }
}
