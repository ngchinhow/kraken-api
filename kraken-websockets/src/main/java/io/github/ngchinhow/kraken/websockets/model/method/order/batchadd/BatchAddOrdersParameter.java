package io.github.ngchinhow.kraken.websockets.model.method.order.batchadd;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websockets.model.method.PrivateParameterInterface;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderCreationInput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class BatchAddOrdersParameter implements PrivateParameterInterface {
    private ZonedDateTime deadline;
    @JsonProperty(required = true)
    private List<BaseOrderCreationInput> orders;
    private String symbol;
    private String token;
    private Boolean validate;
}
