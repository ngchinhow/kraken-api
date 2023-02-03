package org.trading.krakenapi.websocket.dto.publication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.trading.krakenapi.websocket.enums.WebSocketEnumerations;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class PublicationMessage {
    private int channelId;
    private WebSocketEnumerations.CHANNEL channelName;
    private String pair;
}
