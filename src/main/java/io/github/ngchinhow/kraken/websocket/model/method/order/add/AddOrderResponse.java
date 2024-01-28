package io.github.ngchinhow.kraken.websocket.model.method.order.add;

import io.github.ngchinhow.kraken.websocket.model.method.AbstractInteractionResponse;
import io.github.ngchinhow.kraken.websocket.model.method.order.CreatedOrderResult;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
public final class AddOrderResponse extends AbstractInteractionResponse<CreatedOrderResult> {
}
