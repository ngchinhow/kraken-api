package com.kraken.api.javawrapper.websocket.model.event.request;

import com.kraken.api.javawrapper.websocket.dto.PingRequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.event.AbstractInteractiveMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT.PING;


@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
public class PingMessage extends AbstractInteractiveMessage {
    private final String event = PING;

    public PingRequestIdentifier toRequestIdentifier() {
        return new PingRequestIdentifier().toBuilder()
            .reqId(this.getReqId())
            .build();
    }
}
