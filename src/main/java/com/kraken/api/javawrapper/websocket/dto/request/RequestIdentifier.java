package com.kraken.api.javawrapper.websocket.dto.request;

import lombok.*;

import java.math.BigInteger;

@Data
@EqualsAndHashCode
@Builder(toBuilder = true, builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
public class RequestIdentifier {
    private String method;
    private String channel;
    private String symbol;
    private BigInteger requestId;
}
