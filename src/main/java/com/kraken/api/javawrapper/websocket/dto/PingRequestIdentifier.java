package com.kraken.api.javawrapper.websocket.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class PingRequestIdentifier extends RequestIdentifier {

    @Override
    public boolean equals(RequestIdentifier requestIdentifier) {
        return this.getReqId().equals(requestIdentifier.getReqId());
    }
}
