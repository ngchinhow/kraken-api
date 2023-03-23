package com.kraken.api.javawrapper.websocket.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT_TYPE.UNSUBSCRIBE;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class UnsubscribeRequestIdentifier extends BaseSubscriptionRequestIdentifier {

    {
        this.setEvent(UNSUBSCRIBE);
    }

    public SubscribeRequestIdentifier toSubscribeRequestIdentifier() {
        return SubscribeRequestIdentifier.builder()
            .reqId(this.getReqId())
            .channel(this.getChannel())
            .pair(this.getPair())
            .depth(this.getDepth())
            .interval(this.getInterval())
            .build();
    }
}
