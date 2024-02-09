package io.github.ngchinhow.kraken.rest.model.userdata;

import feign.Param;
import io.github.ngchinhow.kraken.rest.model.AbstractRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractOrdersRequest implements AbstractRequest {
    @Param("trades")
    private boolean includeTrades;

    @Param("userref")
    private int userRef;
}
