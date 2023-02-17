package com.kraken.api.javawrapper.websocket.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigInteger;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@AllArgsConstructor
public class SystemStatusMessage extends AbstractEventMessage {
    @JsonProperty("connectionID")
    private BigInteger connectionId;
    private WebSocketEnumerations.SYSTEM_STATUS status;
    private String version;

    public SystemStatusMessage() {
        this.setEvent(WebSocketEnumerations.EVENT.SYSTEM_STATUS);
    }
}
