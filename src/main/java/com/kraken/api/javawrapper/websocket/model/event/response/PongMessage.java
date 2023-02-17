package com.kraken.api.javawrapper.websocket.model.event.response;

import com.kraken.api.javawrapper.websocket.dto.request.PingRequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.event.AbstractInteractiveMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT.PONG;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
public class PongMessage extends AbstractInteractiveMessage implements IResponseMessage {

    public PongMessage() {
        this.setEvent(PONG);
    }

    @Override
    public PingRequestIdentifier toRequestIdentifier() {
        return new PingRequestIdentifier().toBuilder()
            .reqId(this.getReqId())
            .build();
    }
}
