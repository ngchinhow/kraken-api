package com.kraken.api.javawrapper.websocket.dto;

import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseSubscriptionRequestIdentifier extends RequestIdentifier {
    private String event;
    private WebSocketEnumerations.CHANNEL channel;
    private String pair;
    private Integer depth;
    private Integer interval;

    @Override
    public boolean equals(RequestIdentifier requestIdentifier) {
        BaseSubscriptionRequestIdentifier identifier = (BaseSubscriptionRequestIdentifier) requestIdentifier;
        return this.event.equals(identifier.getEvent()) &&
            this.channel.equals(identifier.getChannel()) &&
            this.pair.equals(identifier.getPair()) &&
            this.depth.equals(identifier.getDepth()) &&
            this.interval.equals(identifier.getInterval());
    }
}
