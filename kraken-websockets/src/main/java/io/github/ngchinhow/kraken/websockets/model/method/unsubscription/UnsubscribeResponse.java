package io.github.ngchinhow.kraken.websockets.model.method.unsubscription;

import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelResult;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractSubscriptionResponse;
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
public final class UnsubscribeResponse<R extends AbstractChannelResult, P extends AbstractPublicationMessage>
    extends AbstractSubscriptionResponse<R> {
    private String symbol;
    private ReplaySubject<P> publicationMessageReplaySubject;

    {
        this.setMethod(MethodMetadata.MethodType.UNSUBSCRIBE);
    }
}