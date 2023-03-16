package com.kraken.api.javawrapper.websocket.model.event.request;

import com.kraken.api.javawrapper.websocket.dto.request.SubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.event.BaseSubscriptionMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.stream.Collectors;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT_TYPE.SUBSCRIBE;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeMessage extends BaseSubscriptionMessage {
    private List<String> pair;

    {
        this.setEvent(SUBSCRIBE);
    }

    public List<SubscribeRequestIdentifier> toRequestIdentifiers() {
        return this.pair.stream()
            .map(p -> new SubscribeRequestIdentifier().toBuilder()
                .reqId(this.getReqId())
                .channel(this.getSubscription().getName())
                .pair(p)
                .depth(this.getSubscription().getDepth())
                .interval(this.getSubscription().getInterval())
                .build())
            .collect(Collectors.toUnmodifiableList());
    }
}
