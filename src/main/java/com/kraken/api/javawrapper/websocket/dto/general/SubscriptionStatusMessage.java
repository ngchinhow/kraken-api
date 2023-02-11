package com.kraken.api.javawrapper.websocket.dto.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionStatusMessage extends BaseSubscriptionMessage {
    @JsonProperty("channelID")
    private int channelId;
    private String channelName;
    private String pair;
    private WebSocketEnumerations.SUBSCRIPTION_STATUS status;
    private String errorMessage;
    private SubscriptionEmbeddedObject subscription;
}
