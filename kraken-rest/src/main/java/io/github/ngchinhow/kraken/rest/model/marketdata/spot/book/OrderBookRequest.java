package io.github.ngchinhow.kraken.rest.model.marketdata.spot.book;

import io.github.ngchinhow.kraken.rest.model.RequestInterface;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class OrderBookRequest implements RequestInterface {
    @NonNull
    private String pair;
    private Integer count;
}
