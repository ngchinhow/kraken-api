package com.kraken.api.javawrapper.websocket.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT_TYPE.SUBSCRIBE;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class SubscribeRequestIdentifier extends BaseSubscriptionRequestIdentifier {

    {
        this.setEvent(SUBSCRIBE);
    }

    public SubscribeRequestIdentifier toPublicationRequestIdentifier() {
        return SubscribeRequestIdentifier.builder()
            .channel(this.getChannel())
            .pair(this.getPair())
            .depth(this.getDepth())
            .interval(this.getInterval())
            .build();
    }
}
