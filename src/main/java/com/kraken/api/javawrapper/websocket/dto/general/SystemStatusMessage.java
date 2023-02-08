package com.kraken.api.javawrapper.websocket.dto.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigInteger;

@Getter
@Setter
@SuperBuilder
@Jacksonized
public class SystemStatusMessage extends GeneralMessage {
    @JsonProperty("connectionID")
    private BigInteger connectionId;
    private WebSocketEnumerations.SYSTEM_STATUS status;
    private String version;
}
