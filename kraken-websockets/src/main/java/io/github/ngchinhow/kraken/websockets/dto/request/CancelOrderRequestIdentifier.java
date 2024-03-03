package io.github.ngchinhow.kraken.websockets.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderRequestIdentifier extends RequestIdentifier {
    private String orderId;
    private BigInteger orderUserReference;

    public CancelOrderRequestIdentifier(RequestIdentifier requestIdentifier) {
        super(requestIdentifier);
    }
}
