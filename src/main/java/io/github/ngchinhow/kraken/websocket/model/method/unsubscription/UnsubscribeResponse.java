package io.github.ngchinhow.kraken.websocket.model.method.unsubscription;

import io.github.ngchinhow.kraken.websocket.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractInteractionResponse;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
public class UnsubscribeResponse extends AbstractInteractionResponse {
    private PublishSubject<AbstractPublicationMessage> publicationMessagePublishSubject;

    {
        this.setMethod(MethodMetadata.MethodType.UNSUBSCRIBE);
    }
}