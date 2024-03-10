package io.github.ngchinhow.kraken.rest.model.accountdata.balance;

import io.github.ngchinhow.kraken.rest.model.RequestInterface;
import lombok.Builder;

@Builder
public record TradeBalanceRequest(String asset) implements RequestInterface {
}
