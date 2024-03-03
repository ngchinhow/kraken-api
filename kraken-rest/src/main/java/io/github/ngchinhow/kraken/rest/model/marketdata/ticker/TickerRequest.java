package io.github.ngchinhow.kraken.rest.model.marketdata.ticker;

import io.github.ngchinhow.kraken.rest.model.AbstractRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class TickerRequest implements AbstractRequest {
    private String pair;
}
