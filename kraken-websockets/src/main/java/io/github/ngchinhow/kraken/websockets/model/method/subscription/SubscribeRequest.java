package io.github.ngchinhow.kraken.websockets.model.method.subscription;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionRequest;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelParameter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
public final class SubscribeRequest<T extends AbstractChannelParameter> extends AbstractInteractionRequest<T> {

    {
        setMethod(MethodMetadata.MethodType.SUBSCRIBE);
    }
}
