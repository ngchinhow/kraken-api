package io.github.ngchinhow.kraken.rest.model.userdata;

import feign.Param;
import io.github.ngchinhow.kraken.rest.model.RequestInterface;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractOrdersRequest implements RequestInterface {
    @Param("trades")
    private boolean includeTrades;

    @Param("userref")
    private int userRef;
}
