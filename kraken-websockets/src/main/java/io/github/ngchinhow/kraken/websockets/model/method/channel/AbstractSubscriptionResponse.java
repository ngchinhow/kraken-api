package io.github.ngchinhow.kraken.websockets.model.method.channel;

import io.github.ngchinhow.kraken.websockets.dto.request.SubscriptionRequestIdentifier;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter(value = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractSubscriptionResponse<T extends AbstractChannelResult>
    extends AbstractInteractionResponse<T> {
    private String symbol;

    @Override
    public SubscriptionRequestIdentifier toRequestIdentifier() {
        var symbol = this.symbol;
        if (symbol == null) {
            if (getResult() != null && getResult().getSymbol() != null)
                symbol = getResult().getSymbol();
        }

        final var channel = getResult() == null ? null : getResult().getChannel();

        return new SubscriptionRequestIdentifier(super.toRequestIdentifier())
            .toBuilder()
            .channel(channel)
            .symbol(symbol)
            .build();
    }
}
