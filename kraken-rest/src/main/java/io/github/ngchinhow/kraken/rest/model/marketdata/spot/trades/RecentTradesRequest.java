package io.github.ngchinhow.kraken.rest.model.marketdata.spot.trades;

import io.github.ngchinhow.kraken.rest.model.RequestInterface;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class RecentTradesRequest implements RequestInterface {
    @NonNull
    private String pair;
    private ZonedDateTime since;
    private Integer count;
}
