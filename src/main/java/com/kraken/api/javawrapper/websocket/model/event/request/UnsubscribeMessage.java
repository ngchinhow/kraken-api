package com.kraken.api.javawrapper.websocket.model.event.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.dto.request.UnsubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.event.BaseSubscriptionMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.stream.Collectors;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT_TYPE.UNSUBSCRIBE;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class UnsubscribeMessage extends BaseSubscriptionMessage {

    @JsonProperty("channelID")
    private int channelId;

    @JsonProperty("pair")
    private List<String> pairs;

    {
        this.setEvent(UNSUBSCRIBE);
    }

    public List<UnsubscribeRequestIdentifier> toRequestIdentifiers() {
        return this.pairs.stream()
            .map(p -> UnsubscribeRequestIdentifier.builder()
                .reqId(this.getReqId())
                .channel(this.getSubscription().getName())
                .pair(p)
                .depth(this.getSubscription().getDepth())
                .interval(this.getSubscription().getInterval())
                .build())
            .collect(Collectors.toUnmodifiableList());
    }
}
