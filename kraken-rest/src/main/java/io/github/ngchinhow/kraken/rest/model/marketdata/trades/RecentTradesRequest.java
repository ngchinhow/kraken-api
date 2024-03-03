package io.github.ngchinhow.kraken.rest.model.marketdata.trades;

import io.github.ngchinhow.kraken.rest.model.AbstractRequest;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class RecentTradesRequest implements AbstractRequest {
    @NonNull
    private String pair;
    private ZonedDateTime since;
    private Integer count;
}
