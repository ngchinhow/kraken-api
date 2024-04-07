package io.github.ngchinhow.kraken.rest.model.marketdata.spot.ticker;

import io.github.ngchinhow.kraken.rest.model.RequestInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class TickerRequest implements RequestInterface {
    private String pair;
}
