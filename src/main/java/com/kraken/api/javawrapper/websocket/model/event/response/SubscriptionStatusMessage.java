package com.kraken.api.javawrapper.websocket.model.event.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.dto.request.BaseSubscriptionRequestIdentifier;
import com.kraken.api.javawrapper.websocket.dto.request.SubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.dto.request.UnsubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import com.kraken.api.javawrapper.websocket.model.event.BaseSubscriptionMessage;
import com.kraken.api.javawrapper.websocket.model.publication.AbstractPublicationMessage;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT_TYPE.*;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionStatusMessage extends BaseSubscriptionMessage implements IResponseMessage {
    @JsonProperty("channelID")
    private int channelId;
    private String channelName;
    private String pair;
    private WebSocketEnumerations.SUBSCRIPTION_STATUS_TYPE status;
    private String errorMessage;
    private PublishSubject<AbstractPublicationMessage> publicationMessagePublishSubject;

    {
        this.setEvent(SUBSCRIPTION_STATUS);
    }

    @Override
    public BaseSubscriptionRequestIdentifier toRequestIdentifier() {
        String statusValue = this.status.getVSubscriptionStatus();
        // convert from "subscribed" or "unsubscribed" to "subscribe" or "unsubscribe"
        String requestEvent = statusValue.substring(0, statusValue.length() - 1);
        WebSocketEnumerations.CHANNEL channel = this.getSubscription().getName();
        switch (requestEvent) {
            case SUBSCRIBE -> {
                return SubscribeRequestIdentifier.builder()
                    .reqId(this.getReqId())
                    .channel(channel)
                    .pair(this.pair)
                    .depth(this.getSubscription().getDepth())
                    .interval(this.getSubscription().getInterval())
                    .build();
            }
            case UNSUBSCRIBE -> {
                return UnsubscribeRequestIdentifier.builder()
                    .reqId(this.getReqId())
                    .channel(channel)
                    .pair(this.pair)
                    .depth(this.getSubscription().getDepth())
                    .interval(this.getSubscription().getInterval())
                    .build();
            }
            default -> throw new RuntimeException("Unknown Request Event returned from the SubscriptionStatusMessage");
        }
    }
}
