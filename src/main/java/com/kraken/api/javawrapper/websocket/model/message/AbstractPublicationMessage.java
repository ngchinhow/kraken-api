package com.kraken.api.javawrapper.websocket.model.message;

import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.kraken.api.javawrapper.websocket.enums.MethodMetadata.MethodType.SUBSCRIBE;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractPublicationMessage extends AbstractMessage {

    public RequestIdentifier toRequestIdentifier() {
        return RequestIdentifier.builder()
            .method(SUBSCRIBE)
            .channel(this.getChannel())
            .build();
    }
}
