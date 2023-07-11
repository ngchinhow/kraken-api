package io.github.ngchinhow.kraken.websocket.model.method.unsubscription;

import io.github.ngchinhow.kraken.websocket.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractInteractionResponse;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractResult;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public final class UnsubscribeResponse<R extends AbstractResult, P extends AbstractPublicationMessage>
        extends AbstractInteractionResponse<R> {
    private String symbol;
    private ReplaySubject<P> publicationMessageReplaySubject;

    {
        this.setMethod(MethodMetadata.MethodType.UNSUBSCRIBE);
    }
}