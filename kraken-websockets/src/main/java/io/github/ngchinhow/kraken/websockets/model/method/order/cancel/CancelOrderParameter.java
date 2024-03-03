package io.github.ngchinhow.kraken.websockets.model.method.order.cancel;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderParameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public final class CancelOrderParameter extends BaseOrderParameter {
    @JsonProperty(value = "order_id")
    private List<String> orderIds;
    @JsonProperty(value = "order_userref")
    private List<BigInteger> orderUserReferences;
}
