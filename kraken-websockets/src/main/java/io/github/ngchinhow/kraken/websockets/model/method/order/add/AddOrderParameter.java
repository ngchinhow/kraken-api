package io.github.ngchinhow.kraken.websockets.model.method.order.add;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websockets.model.method.PrivateParameterInterface;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderCreationInput;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public final class AddOrderParameter extends BaseOrderCreationInput implements PrivateParameterInterface {
    private ZonedDateTime deadline;
    @JsonProperty(value = "limit_price_type")
    private BaseOrderInput.OrderPriceType limitPriceType;
    @JsonProperty(required = true)
    private String symbol;
    private String token;
    private Boolean validate;
}
