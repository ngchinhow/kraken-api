package io.github.ngchinhow.kraken.websocket.model.method;

import io.github.ngchinhow.kraken.websocket.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websocket.enums.MethodMetadata;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

public abstract class Unsubscription {
    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @Jacksonized
    @NoArgsConstructor
    public static class UnsubscribeRequest extends Interaction.AbstractInteractionRequest {
        {
            this.setMethod(MethodMetadata.MethodType.UNSUBSCRIBE);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @Jacksonized
    @NoArgsConstructor
    public static class UnsubscribeResponse extends Interaction.AbstractInteractionResponse {
        private PublishSubject<AbstractPublicationMessage> publicationMessagePublishSubject;
        {
            this.setMethod(MethodMetadata.MethodType.UNSUBSCRIBE);
        }
    }
}
