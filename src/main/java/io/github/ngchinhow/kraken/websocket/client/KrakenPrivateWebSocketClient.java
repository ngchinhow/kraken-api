package io.github.ngchinhow.kraken.websocket.client;

import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.rest.client.WebSocketsAuthenticationClient;
import io.github.ngchinhow.kraken.rest.model.websocketsauthentication.token.WebSocketsTokenResult;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractParameter;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractResult;
import io.github.ngchinhow.kraken.websocket.model.method.subscription.SubscribeRequest;
import io.github.ngchinhow.kraken.websocket.model.method.subscription.SubscribeResponse;
import io.github.ngchinhow.kraken.websocket.model.method.unsubscription.UnsubscribeRequest;
import io.github.ngchinhow.kraken.websocket.model.method.unsubscription.UnsubscribeResponse;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static io.github.ngchinhow.kraken.properties.KrakenProperties.KRAKEN_WEBSOCKET_API_PRIVATE_URL;

@Slf4j
public final class KrakenPrivateWebSocketClient extends KrakenBaseWebSocketClient {
    private final WebSocketsAuthenticationClient authenticationClient;

    public KrakenPrivateWebSocketClient(MarketDataClient marketDataClient, WebSocketsAuthenticationClient authenticationClient) {
        super(KRAKEN_WEBSOCKET_API_PRIVATE_URL, marketDataClient);
        this.authenticationClient = authenticationClient;
    }

    @Override
    public <R extends AbstractResult, P extends AbstractPublicationMessage, T extends AbstractParameter>
    List<Single<SubscribeResponse<R, P>>> subscribe(SubscribeRequest<T> subscribeRequest) {
        WebSocketsTokenResult tokenResponse = authenticationClient.getWebsocketsToken();
        T param = subscribeRequest.getParams();
        param.setToken(tokenResponse.getToken());
        subscribeRequest.setParams(param);
        return super.subscribe(subscribeRequest);
    }

    @Override
    public <R extends AbstractResult, P extends AbstractPublicationMessage, T extends AbstractParameter>
    List<Single<UnsubscribeResponse<R, P>>> unsubscribe(UnsubscribeRequest<T> unsubscribeRequest) {
        WebSocketsTokenResult tokenResponse = authenticationClient.getWebsocketsToken();
        T param = unsubscribeRequest.getParams();
        param.setToken(tokenResponse.getToken());
        unsubscribeRequest.setParams(param);
        return super.unsubscribe(unsubscribeRequest);
    }
}
