package io.github.ngchinhow.kraken.websockets.model.method;

import io.github.ngchinhow.kraken.websockets.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websockets.dto.request.SubscriptionRequestIdentifier;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelParameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractInteractionRequest<T extends ParameterInterface> extends AbstractRequest {
    private T params;

    public List<RequestIdentifier> toRequestIdentifiers(ZonedDateTime timestamp) {
        var requestIdentifier = super.toRequestIdentifier(timestamp);
        if (params instanceof AbstractChannelParameter channelParameter)
            return channelParameter.getSymbols()
                                   .stream()
                                   .map(e -> new SubscriptionRequestIdentifier(requestIdentifier)
                                       .toBuilder()
                                       .channel(channelParameter.getChannel())
                                       .symbol(e)
                                       .build())
                                   .collect(Collectors.toUnmodifiableList());
        else
            return List.of(requestIdentifier);
    }
}