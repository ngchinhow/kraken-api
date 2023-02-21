package com.kraken.api.javawrapper.websocket.model.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT_TYPE.HEARTBEAT;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
public class HeartbeatMessage extends AbstractEventMessage {

    {
        this.setEvent(HEARTBEAT);
    }
}
