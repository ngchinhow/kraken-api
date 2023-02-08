package com.kraken.api.javawrapper.websocket.dto.publication;

import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class PublicationMessage {
    private int channelId;
    private WebSocketEnumerations.CHANNEL channelName;
    private String pair;
}
