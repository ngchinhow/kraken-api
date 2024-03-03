package io.github.ngchinhow.kraken.websockets.model.method.subscription;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractSubscriptionRequest;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelParameter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public final class SubscribeRequest<T extends AbstractChannelParameter> extends AbstractSubscriptionRequest<T> {

    {
        setMethod(MethodMetadata.MethodType.SUBSCRIBE);
    }
}
