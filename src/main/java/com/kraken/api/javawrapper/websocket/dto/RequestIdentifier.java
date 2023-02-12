package com.kraken.api.javawrapper.websocket.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class RequestIdentifier {
    private BigInteger reqId;

    public abstract boolean equals(RequestIdentifier requestIdentifier);
}
