package org.trading.krakenapi.websocket.dto.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.trading.krakenapi.websocket.enums.WebSocketEnumerations;

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
