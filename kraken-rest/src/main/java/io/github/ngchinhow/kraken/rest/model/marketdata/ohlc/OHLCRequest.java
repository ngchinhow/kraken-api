package io.github.ngchinhow.kraken.rest.model.marketdata.ohlc;

import io.github.ngchinhow.kraken.rest.model.AbstractRequest;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class OHLCRequest implements AbstractRequest {
    @NonNull
    private String pair;
    private Integer interval;
    private Long since;
}
