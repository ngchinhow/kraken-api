package io.github.ngchinhow.kraken.websocket.model.method.subscription;

import io.github.ngchinhow.kraken.websocket.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractInteractionResponse;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractResult;
import io.reactivex.rxjava3.subjects.PublishSubject;
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
public class SubscribeResponse<R extends AbstractResult, P extends AbstractPublicationMessage>
        extends AbstractInteractionResponse<R> {
    private PublishSubject<P> publicationMessagePublishSubject;

    {
        this.setMethod(MethodMetadata.MethodType.SUBSCRIBE);
    }
}