package io.github.ngchinhow.kraken.websockets.model.method;

import io.github.ngchinhow.kraken.websockets.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websockets.dto.request.SubscriptionRequestIdentifier;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelParameter;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@SuperBuilder
public abstract class AbstractSubscriptionRequest<T extends AbstractChannelParameter>
    extends AbstractInteractionRequest<T> {

    @Override
    public List<RequestIdentifier> toRequestIdentifiers(ZonedDateTime timestamp) {
        var requestIdentifier = super.toRequestIdentifier(timestamp);
        var params = getParams();
        return params.getSymbols()
                     .stream()
                     .map(e -> new SubscriptionRequestIdentifier(requestIdentifier)
                         .toBuilder()
                         .channel(params.getChannel())
                         .symbol(e)
                         .build())
                     .collect(Collectors.toUnmodifiableList());
    }
}
