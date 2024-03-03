package io.github.ngchinhow.kraken.websockets.model.method.order.batchadd;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderParameter;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderCreationInput;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public final class BatchAddOrdersParameter extends BaseOrderParameter {
    private ZonedDateTime deadline;
    @JsonProperty(required = true)
    private List<BaseOrderCreationInput> orders;
    private String symbol;
    private Boolean validate;
}
