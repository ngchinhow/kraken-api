package io.github.ngchinhow.kraken.websockets.model.method.order.add;

import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.CreatedOrderResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
public final class AddOrderResponse extends AbstractInteractionResponse<CreatedOrderResult> {
}
