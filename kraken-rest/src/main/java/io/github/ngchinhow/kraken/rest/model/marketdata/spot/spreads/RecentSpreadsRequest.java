package io.github.ngchinhow.kraken.rest.model.marketdata.spot.spreads;

import io.github.ngchinhow.kraken.rest.model.RequestInterface;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentSpreadsRequest implements RequestInterface {
    @NonNull
    private String pair;
    private ZonedDateTime since;
}
