package com.kraken.api.javawrapper.websocket.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT.SUBSCRIBE;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class SubscribeRequestIdentifier extends BaseSubscriptionRequestIdentifier {

    public SubscribeRequestIdentifier() {
        this.setEvent(SUBSCRIBE);
    }

    public SubscribeRequestIdentifier toPublicationRequestIdentifier() {
        return new SubscribeRequestIdentifier().toBuilder()
            .channel(this.getChannel())
            .pair(this.getPair())
            .depth(this.getDepth())
            .interval(this.getInterval())
            .build();
    }
}
