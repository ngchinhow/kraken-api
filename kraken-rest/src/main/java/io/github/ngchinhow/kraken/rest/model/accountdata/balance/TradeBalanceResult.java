package io.github.ngchinhow.kraken.rest.model.accountdata.balance;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.rest.model.ResultInterface;

import java.math.BigDecimal;

public record TradeBalanceResult(@JsonProperty("eb") BigDecimal equivalentBalance,
                                 @JsonProperty("tb") BigDecimal tradeBalance,
                                 @JsonProperty("m") BigDecimal marginAmount,
                                 @JsonProperty("n") BigDecimal netProfitLoss,
                                 @JsonProperty("c") BigDecimal cost,
                                 @JsonProperty("v") BigDecimal floatingValuation,
                                 @JsonProperty("e") BigDecimal equity,
                                 @JsonProperty("mf") BigDecimal freeMargin,
                                 @JsonProperty("ml") BigDecimal marginLevel,
                                 @JsonProperty("uv") BigDecimal unexecutedValue) implements ResultInterface {
}
