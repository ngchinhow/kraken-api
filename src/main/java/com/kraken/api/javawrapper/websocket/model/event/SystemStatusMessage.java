package com.kraken.api.javawrapper.websocket.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigInteger;

@Getter
@Setter
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class SystemStatusMessage extends AbstractEventMessage {
    @JsonProperty("connectionID")
    private BigInteger connectionId;
    private WebSocketEnumerations.SYSTEM_STATUS status;
    private String version;
}
