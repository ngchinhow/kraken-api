package io.github.ngchinhow.kraken.websockets.model.method.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseOrderId implements OrderIdInterface {
    @NonNull
    @JsonProperty("order_id")
    private String orderId;
}