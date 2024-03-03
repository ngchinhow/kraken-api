package io.github.ngchinhow.kraken.websockets.dto.request;


import lombok.*;
import lombok.experimental.SuperBuilder;

import static io.github.ngchinhow.kraken.websockets.enums.MethodMetadata.MethodType.SUBSCRIBE;

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

    /**
     * Publication messages do not have req_id information, so the {@link RequestIdentifier} cannot have this field as
     * key in the map.
     *
     * @return A new {@link SubscriptionRequestIdentifier} with reqId set to null.
     */
    public SubscriptionRequestIdentifier buildForPublicationMessages() {
        return SubscriptionRequestIdentifier.builder()
                                            .method(getMethod())
                                            .channel(channel)
                                            .symbol(symbol)
                                            .timestamp(getTimestamp())
                                            .build();
    }

    /**
     * Already subscribed error responses do not have channel and symbol information, so the {@link RequestIdentifier}
     * cannot have these fields as key in the map.
     *
     * @return A new {@link SubscriptionRequestIdentifier} with channel and symbol set to null.
     */
    public SubscriptionRequestIdentifier buildForAlreadySubscribed() {
        return SubscriptionRequestIdentifier.builder()
                                            .requestId(getRequestId())
                                            .method(getMethod())
                                            .timestamp(getTimestamp())
                                            .build();
    }

    /**
     * No subscription found error responses do not have channel information, so the {@link RequestIdentifier} cannot
     * have this field as key in the map.
     *
     * @return A new {@link SubscriptionRequestIdentifier} with channel set to null.
     */
    public SubscriptionRequestIdentifier buildForNoSubscriptionFound() {
        return SubscriptionRequestIdentifier.builder()
                                            .requestId(getRequestId())
                                            .symbol(getSymbol())
                                            .method(SUBSCRIBE)
                                            .timestamp(getTimestamp())
                                            .build();
    }

    /**
     * No symbol found error responses do not have channel information, so the {@link RequestIdentifier} cannot have
     * this field as key in the map.
     *
     * @return A new {@link SubscriptionRequestIdentifier} with channel set to null.
     */
    public SubscriptionRequestIdentifier buildForNoSymbolFound() {
        return SubscriptionRequestIdentifier.builder()
                                            .requestId(getRequestId())
                                            .symbol(getSymbol())
                                            .method(getMethod())
                                            .timestamp(getTimestamp())
                                            .build();
    }
}
