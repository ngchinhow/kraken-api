package io.github.ngchinhow.kraken.websockets.model.message;

import io.github.ngchinhow.kraken.websockets.dto.request.SubscriptionRequestIdentifier;
import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractPublicationMessage extends AbstractMessage {

    public SubscriptionRequestIdentifier toRequestIdentifier() {
        return SubscriptionRequestIdentifier.builder()
                                            .method(MethodMetadata.MethodType.SUBSCRIBE)
                                            .channel(this.getChannel())
                                            .build();
    }
}
