package com.kraken.api.javawrapper.websocket.model.event.request;

import com.kraken.api.javawrapper.websocket.dto.UnsubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.event.BaseSubscriptionMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class UnsubscribeMessage extends BaseSubscriptionMessage {
    private List<String> pair;

    public List<UnsubscribeRequestIdentifier> toRequestIdentifier() {
        return this.pair.stream()
            .map(p -> new UnsubscribeRequestIdentifier().toBuilder()
                .reqId(this.getReqId())
                .channel(this.getSubscription().getName())
                .pair(p)
                .depth(this.getSubscription().getDepth())
                .interval(this.getSubscription().getInterval())
                .build())
            .collect(Collectors.toUnmodifiableList());
    }
}
