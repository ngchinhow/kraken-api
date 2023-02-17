package com.kraken.api.javawrapper.websocket.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class RequestIdentifier {
    private BigInteger reqId;

    private String event;
}
