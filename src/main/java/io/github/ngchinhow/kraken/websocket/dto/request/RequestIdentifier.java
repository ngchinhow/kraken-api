package io.github.ngchinhow.kraken.websocket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;
import java.time.ZonedDateTime;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RequestIdentifier {
    private String method;
    private BigInteger requestId;
    @EqualsAndHashCode.Exclude
    private ZonedDateTime timestamp;

    public RequestIdentifier(RequestIdentifier requestIdentifier) {
        this.method = requestIdentifier.getMethod();
        this.requestId = requestIdentifier.getRequestId();
        this.timestamp = requestIdentifier.getTimestamp();
    }
}
