package io.github.ngchinhow.kraken.websockets.model.method.order.edit;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websockets.model.method.PrivateParameterInterface;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderInput;
import io.github.ngchinhow.kraken.websockets.model.method.order.OrderIdInterface;
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
public final class EditOrderParameter extends BaseOrderInput implements PrivateParameterInterface, OrderIdInterface {
    private ZonedDateTime deadline;
    @JsonProperty(value = "order_id")
    private String orderId;
    private String symbol;
    private String token;
    private Boolean validate;
}
