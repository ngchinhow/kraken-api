package com.kraken.api.javawrapper.websocket.model.event.response;

import com.kraken.api.javawrapper.websocket.dto.PingRequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.event.AbstractInteractiveMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
public class PongMessage extends AbstractInteractiveMessage implements IResponseMessage {

    @Override
    public PingRequestIdentifier toRequestIdentifier() {
        return new PingRequestIdentifier().toBuilder()
            .reqId(this.getReqId())
            .build();
    }
}
