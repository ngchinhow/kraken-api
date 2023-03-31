package com.kraken.api.javawrapper.websocket.model.message;

import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractPublicationMessage extends AbstractMessage {

    public abstract RequestIdentifier toSubscribeRequestIdentifier();
}
