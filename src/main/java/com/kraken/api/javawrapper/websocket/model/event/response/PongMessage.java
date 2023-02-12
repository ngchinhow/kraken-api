package com.kraken.api.javawrapper.websocket.model.event.response;

import com.kraken.api.javawrapper.websocket.dto.PingRequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.event.AbstractInteractiveMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@SuperBuilder
@Jacksonized
public class PongMessage extends AbstractInteractiveMessage {

    public PingRequestIdentifier toRequestIdentifier() {
        return PingRequestIdentifier.builder()
            .reqId(this.getReqId())
            .build();
    }
}
