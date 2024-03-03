package io.github.ngchinhow.kraken.rest.model.marketdata.book;

import io.github.ngchinhow.kraken.rest.model.AbstractRequest;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class OrderBookRequest implements AbstractRequest {
    @NonNull
    private String pair;
    private Integer count;
}
