package io.github.ngchinhow.kraken.websockets.model.method.order;

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
public class EditedOrderResult extends BaseOrderId {
    @NonNull
    @JsonProperty("original_order_id")
    private String originalOrderId;
}
