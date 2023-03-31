package com.kraken.api.javawrapper.websocket.client;

import com.kraken.api.javawrapper.rest.client.WebSocketsAuthenticationClient;
import lombok.extern.slf4j.Slf4j;

import static com.kraken.api.javawrapper.properties.KrakenProperties.KRAKEN_WEBSOCKET_API_PRIVATE_URL;

@Slf4j
public class KrakenPrivateWebSocketClient extends KrakenBaseWebSocketClient {
    private final WebSocketsAuthenticationClient authenticationClient;

    public KrakenPrivateWebSocketClient(WebSocketsAuthenticationClient authenticationClient) {
        super(KRAKEN_WEBSOCKET_API_PRIVATE_URL);
        this.authenticationClient = authenticationClient;
    }

//    @Override
//    public List<Single<SubscriptionStatusMessage>> subscribe(SubscribeRequestMessage subscribeRequestMessage) {
//        KrakenResponse<WebSocketsTokenResult> tokenResponse = authenticationClient.getWebsocketsToken();
//        if (Objects.nonNull(tokenResponse.getResult()))
//            subscribeRequestMessage.setSubscription(
//                subscribeRequestMessage.getSubscription()
//                    .toBuilder()
//                    .token(tokenResponse.getResult().getToken())
//                    .build()
//            );
//        else if (Objects.nonNull(tokenResponse.getError())) {
//            String message = "Unable to retrieve token for private WebSockets authentication. Errors are: \n" +
//                String.join("\n", tokenResponse.getError());
//            log.error(message);
//            throw new RuntimeException(message);
//        }
//        return super.subscribe(subscribeRequestMessage);
//    }

//    @Override
//    public List<Single<SubscriptionStatusMessage>> unsubscribe(UnsubscribeRequestMessage unsubscribeRequestMessage) {
//        KrakenResponse<WebSocketsTokenResult> tokenResponse = authenticationClient.getWebsocketsToken();
//        if (Objects.nonNull(tokenResponse.getResult()))
//            unsubscribeRequestMessage.setSubscription(
//                unsubscribeRequestMessage.getSubscription()
//                    .toBuilder()
//                    .token(tokenResponse.getResult().getToken())
//                    .build()
//            );
//        else if (Objects.nonNull(tokenResponse.getError())) {
//            String message = "Unable to retrieve token for private WebSockets authentication. Errors are: \n" +
//                String.join("\n", tokenResponse.getError());
//            log.error(message);
//            throw new RuntimeException(message);
//        }
//        return super.unsubscribe(unsubscribeRequestMessage);
//    }
}
