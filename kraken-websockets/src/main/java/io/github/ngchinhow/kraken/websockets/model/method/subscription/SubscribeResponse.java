package io.github.ngchinhow.kraken.websockets.model.method.subscription;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelResult;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractSubscriptionResponse;
import io.github.ngchinhow.kraken.websockets.model.method.unsubscription.UnsubscribeResponse;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public final class SubscribeResponse<R extends AbstractChannelResult, P extends AbstractPublicationMessage>
    extends AbstractSubscriptionResponse<R> {
    private ReplaySubject<P> publicationMessageReplaySubject;

    {
        setMethod(MethodMetadata.MethodType.SUBSCRIBE);
    }

    public UnsubscribeResponse<R> toUnsubscribeResponse() {
        return UnsubscribeResponse.<R>builder()
                                  .requestId(getRequestId())
                                  .timeIn(getTimeIn())
                                  .timeOut(getTimeOut())
                                  .error(getError())
                                  .success(getSuccess())
                                  .result(getResult())
                                  .symbol(getSymbol())
                                  .build();
    }
}