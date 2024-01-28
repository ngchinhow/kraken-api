package io.github.ngchinhow.kraken.websocket.model.method;

import io.github.ngchinhow.kraken.websocket.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websocket.dto.request.SubscriptionRequestIdentifier;
import io.github.ngchinhow.kraken.websocket.model.method.channel.ChannelParameterInterface;
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
        if (params instanceof ChannelParameterInterface channelParameterInterface)
            return channelParameterInterface.getSymbols()
                                            .stream()
                                            .map(e -> new SubscriptionRequestIdentifier(requestIdentifier)
                                                .toBuilder()
                                                .channel(channelParameterInterface.getChannel())
                                                .symbol(e)
                                                .build())
                                            .collect(Collectors.toUnmodifiableList());
        else
            return List.of(requestIdentifier);
    }
}