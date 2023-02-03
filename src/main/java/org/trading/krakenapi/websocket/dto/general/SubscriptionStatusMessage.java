package org.trading.krakenapi.websocket.dto.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.trading.krakenapi.websocket.enums.WebSocketEnumerations;

@Getter
@Setter
@SuperBuilder
@Jacksonized
@NoArgsConstructor
public class SubscriptionStatusMessage extends BaseSubscriptionMessage {
    @JsonProperty("channelID")
    private int channelId;
    private String channelName;
    private WebSocketEnumerations.SUBSCRIPTION_STATUS status;
    private String errorMessage;
    private SubscriptionEmbeddedObject subscription;
}
