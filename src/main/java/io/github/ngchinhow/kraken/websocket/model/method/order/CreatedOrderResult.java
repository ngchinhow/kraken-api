package io.github.ngchinhow.kraken.websocket.model.method.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public final class CreatedOrderResult extends BaseOrderId {
    @NonNull
    @JsonProperty("order_userref")
    private String orderUserReference;
}
