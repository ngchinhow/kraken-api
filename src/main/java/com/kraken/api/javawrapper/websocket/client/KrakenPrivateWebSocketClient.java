package com.kraken.api.javawrapper.websocket.client;

import com.kraken.api.javawrapper.rest.client.WebSocketsAuthenticationClient;
import com.kraken.api.javawrapper.rest.dto.KrakenResponse;
import com.kraken.api.javawrapper.rest.dto.websocketsauthentication.WebSocketsTokenResult;
import com.kraken.api.javawrapper.websocket.dto.response.SubscribedObject;
import com.kraken.api.javawrapper.websocket.model.event.request.SubscribeMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

import static com.kraken.api.javawrapper.properties.KrakenProperties.KRAKEN_WEBSOCKET_API_PRIVATE_URL;

@Slf4j
public class KrakenPrivateWebSocketClient extends KrakenBaseWebSocketClient {
    private final WebSocketsAuthenticationClient authenticationClient;

    public KrakenPrivateWebSocketClient(WebSocketsAuthenticationClient authenticationClient) {
        super(KRAKEN_WEBSOCKET_API_PRIVATE_URL);
        this.authenticationClient = authenticationClient;
    }

    @Override
    public List<SubscribedObject> subscribe(SubscribeMessage subscribeMessage) {
        KrakenResponse<WebSocketsTokenResult> tokenResponse = authenticationClient.getWebsocketsToken();
        if (Objects.nonNull(tokenResponse.getResult()))
            subscribeMessage.setSubscription(
                subscribeMessage.getSubscription()
                    .toBuilder()
                    .token(tokenResponse.getResult().getToken())
                    .build()
            );
        else if (Objects.nonNull(tokenResponse.getError())) {
            String message = "Unable to retrieve token for private WebSockets authentication. Errors are: \n" +
                String.join("\n", tokenResponse.getError());
            log.error(message);
            throw new RuntimeException(message);
        }
        return super.subscribe(subscribeMessage);
    }
}
