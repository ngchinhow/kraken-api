package io.github.ngchinhow.kraken.rest.model.marketdata.ohlc;

import io.github.ngchinhow.kraken.rest.model.AbstractRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor(force = true)
public class OHLCRequest implements AbstractRequest {
    @NonNull
    private String pair;
    private Integer interval;
    private Long since;
}
