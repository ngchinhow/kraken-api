package com.kraken.api.javawrapper.websocket.model.event.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.dto.BaseSubscriptionRequestIdentifier;
import com.kraken.api.javawrapper.websocket.dto.SubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.dto.UnsubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import com.kraken.api.javawrapper.websocket.model.event.BaseSubscriptionMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT.SUBSCRIBE;
import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT.UNSUBSCRIBE;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionStatusMessage extends BaseSubscriptionMessage implements IResponseMessage {
    @JsonProperty("channelID")
    private int channelId;
    private String channelName;
    private String pair;
    private WebSocketEnumerations.SUBSCRIPTION_STATUS status;
    private String errorMessage;

    @Override
    public BaseSubscriptionRequestIdentifier toRequestIdentifier() {
        String statusValue = this.status.getVSubscriptionStatus();
        // convert from "subscribed" or "unsubscribed" to "subscribe" or "unsubscribe"
        String requestEvent = statusValue.substring(0, statusValue.length() - 2);
        WebSocketEnumerations.CHANNEL channel = WebSocketEnumerations.CHANNEL.getEChannel(this.channelName.split("-")[0]);
        switch (requestEvent) {
            case SUBSCRIBE -> {
                return new SubscribeRequestIdentifier().toBuilder()
                    .reqId(this.getReqId())
                    .channel(channel)
                    .pair(this.pair)
                    .depth(this.getSubscription().getDepth())
                    .interval(this.getSubscription().getInterval())
                    .build();
            }
            case UNSUBSCRIBE -> {
                return new UnsubscribeRequestIdentifier().toBuilder()
                    .reqId(this.getReqId())
                    .channel(channel)
                    .pair(this.pair)
                    .depth(this.getSubscription().getDepth())
                    .interval(this.getSubscription().getInterval())
                    .build();
            }
        }
        return null;
    }
}
