package io.github.ngchinhow.kraken.websockets.model.method.order.cancelallafter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderParameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public final class CancelAllOrdersAfterParameter extends BaseOrderParameter {
    @JsonProperty(required = true)
    private Integer timeout;
}
