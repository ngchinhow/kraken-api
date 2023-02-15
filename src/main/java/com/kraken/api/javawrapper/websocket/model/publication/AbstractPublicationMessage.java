package com.kraken.api.javawrapper.websocket.model.publication;

import com.kraken.api.javawrapper.websocket.dto.SubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractPublicationMessage {
    private int channelId;
    private WebSocketEnumerations.CHANNEL channelName;
    private String pair;

    public SubscribeRequestIdentifier toSubscribeRequestIdentifier() {
        return new SubscribeRequestIdentifier().toBuilder()
            .channel(this.channelName)
            .pair(this.pair)
            .build();
    }
}
