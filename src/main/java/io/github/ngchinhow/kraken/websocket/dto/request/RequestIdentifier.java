package io.github.ngchinhow.kraken.websocket.dto.request;


import lombok.*;

import java.math.BigInteger;
import java.time.ZonedDateTime;

@Data
@Builder(toBuilder = true, builderClassName = "Builder")
@AllArgsConstructor
public class RequestIdentifier {
    private String method;
    private String channel;
    private String symbol;
    private BigInteger requestId;
    @EqualsAndHashCode.Exclude
    private ZonedDateTime timestamp;

    public RequestIdentifier duplicate() {
        return RequestIdentifier.builder()
            .method(method)
            .channel(channel)
            .symbol(symbol)
            .build();
    }
}
