package com.kraken.api.javawrapper.websocket.model.event.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.dto.BaseSubscriptionRequestIdentifier;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import com.kraken.api.javawrapper.websocket.model.event.BaseSubscriptionMessage;
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

    public BaseSubscriptionRequestIdentifier toRequestIdentifier() {
        String statusValue = this.status.getVSubscriptionStatus();
        String requestEvent = statusValue.substring(0, statusValue.length() - 2);
        return BaseSubscriptionRequestIdentifier.builder()
            .reqId(this.getReqId())
            .event(requestEvent)
            .channel(WebSocketEnumerations.CHANNEL.getEChannel(this.channelName.split("-")[0]))
            .pair(this.pair)
            .depth(this.getSubscription().getDepth())
            .interval(this.getSubscription().getInterval())
            .build();
    }
}
