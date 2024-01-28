package io.github.ngchinhow.kraken.websocket.model.method.channel;

import io.github.ngchinhow.kraken.websocket.dto.request.SubscriptionRequestIdentifier;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractInteractionResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter(value = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractSubscriptionResponse<T extends AbstractChannelResult>
    extends AbstractInteractionResponse<T> {

    @Override
    public SubscriptionRequestIdentifier toRequestIdentifier() {
        return new SubscriptionRequestIdentifier(super.toRequestIdentifier())
                    .toBuilder()
                    .channel(getResult().getChannel())
                    .symbol(getResult().getSymbol())
                    .build();
    }
}
