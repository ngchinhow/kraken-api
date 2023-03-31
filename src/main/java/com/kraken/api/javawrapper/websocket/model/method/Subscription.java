package com.kraken.api.javawrapper.websocket.model.method;

import com.kraken.api.javawrapper.websocket.model.message.AbstractPublicationMessage;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import static com.kraken.api.javawrapper.websocket.enums.MethodMetadata.MethodType.SUBSCRIBE;

public abstract class Subscription {
    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @Jacksonized
    @NoArgsConstructor
    public static class SubscribeRequest extends Interaction.AbstractInteractionRequest {

        {
            this.setMethod(SUBSCRIBE);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @Jacksonized
    @NoArgsConstructor
    public static class SubscribeResponse extends Interaction.AbstractInteractionResponse {
        private PublishSubject<AbstractPublicationMessage> publicationMessagePublishSubject;

        {
            this.setMethod(SUBSCRIBE);
        }
    }
}
