package io.github.ngchinhow.kraken.websockets.model.method.order.batchcancel;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderParameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public final class BatchCancelOrdersParameter extends BaseOrderParameter {
    @JsonProperty(required = true)
    private List<String> orders;
}
