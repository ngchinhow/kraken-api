package io.github.ngchinhow.kraken.websockets.dto.request;


import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequestIdentifier extends RequestIdentifier {
    private String channel;
    private String symbol;

    public SubscriptionRequestIdentifier(RequestIdentifier requestIdentifier) {
        super(requestIdentifier);
    }

    public SubscriptionRequestIdentifier duplicate() {
        return SubscriptionRequestIdentifier.builder()
                                            .method(getMethod())
                                            .channel(channel)
                                            .symbol(symbol)
                                            .build();
    }
}
