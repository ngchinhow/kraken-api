package io.github.ngchinhow.kraken.websockets.model.method.order.edit;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderId;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class EditOrderResult extends BaseOrderId {
    @NonNull
    @JsonProperty("original_order_id")
    private String originalOrderId;
}
