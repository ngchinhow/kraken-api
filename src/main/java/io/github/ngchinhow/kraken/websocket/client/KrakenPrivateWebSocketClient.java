package io.github.ngchinhow.kraken.websocket.client;

import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.rest.client.WebSocketsAuthenticationClient;
import io.github.ngchinhow.kraken.rest.model.KrakenResponse;
import io.github.ngchinhow.kraken.rest.model.websocketsauthentication.WebSocketsToken;
import io.github.ngchinhow.kraken.websocket.model.method.Subscription;
import io.github.ngchinhow.kraken.websocket.model.method.Unsubscription;
import io.github.ngchinhow.kraken.websocket.model.method.detail.AbstractParameter;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

import static io.github.ngchinhow.kraken.properties.KrakenProperties.KRAKEN_WEBSOCKET_API_PRIVATE_URL;

@Slf4j
public final class KrakenPrivateWebSocketClient extends KrakenBaseWebSocketClient {
    private final WebSocketsAuthenticationClient authenticationClient;

    public KrakenPrivateWebSocketClient(MarketDataClient marketDataClient, WebSocketsAuthenticationClient authenticationClient) {
        super(KRAKEN_WEBSOCKET_API_PRIVATE_URL, marketDataClient);
        this.authenticationClient = authenticationClient;
    }

    @Override
    public List<Single<Subscription.SubscribeResponse>> subscribe(Subscription.SubscribeRequest subscribeRequest) {
        KrakenResponse<WebSocketsToken.Result> tokenResponse = authenticationClient.getWebsocketsToken();
        if (Objects.nonNull(tokenResponse.getResult())) {
            AbstractParameter param = subscribeRequest.getParams();
            param.setToken(tokenResponse.getResult().getToken());
            subscribeRequest.setParams(param);
        } else if (Objects.nonNull(tokenResponse.getError())) {
            String message = "Unable to retrieve token for private WebSockets authentication. Errors are: \n" +
                String.join("\n", tokenResponse.getError());
            log.error(message);
            throw new RuntimeException(message);
        }
        return super.subscribe(subscribeRequest);
    }

    @Override
    public List<Single<Unsubscription.UnsubscribeResponse>> unsubscribe(Unsubscription.UnsubscribeRequest unsubscribeRequest) {
        KrakenResponse<WebSocketsToken.Result> tokenResponse = authenticationClient.getWebsocketsToken();
        if (Objects.nonNull(tokenResponse.getResult())) {
            AbstractParameter param = unsubscribeRequest.getParams();
            param.setToken(tokenResponse.getResult().getToken());
            unsubscribeRequest.setParams(param);
        } else if (Objects.nonNull(tokenResponse.getError())) {
            String message = "Unable to retrieve token for private WebSockets authentication. Errors are: \n" +
                String.join("\n", tokenResponse.getError());
            log.error(message);
            throw new RuntimeException(message);
        }
        return super.unsubscribe(unsubscribeRequest);
    }
}
