package com.kraken.api.javawrapper.websocket.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT.PING;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class PingRequestIdentifier extends RequestIdentifier {

    public PingRequestIdentifier() {
        this.setEvent(PING);
    }
}
