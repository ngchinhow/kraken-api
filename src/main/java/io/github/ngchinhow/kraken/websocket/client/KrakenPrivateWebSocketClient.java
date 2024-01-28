package io.github.ngchinhow.kraken.websocket.client;

import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.rest.client.WebSocketsAuthenticationClient;
import io.github.ngchinhow.kraken.rest.model.websocketsauthentication.token.WebSocketsTokenResult;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websocket.model.method.ParameterInterface;
import io.github.ngchinhow.kraken.websocket.model.method.PrivateParameterInterface;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelResult;
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

    public <R extends AbstractChannelResult, P extends AbstractPublicationMessage, T extends ParameterInterface>
    List<Single<SubscribeResponse<R, P>>> subscribe(SubscribeRequest<T> subscribeRequest) {
        if (subscribeRequest.getParams() instanceof PrivateParameterInterface privateParameter)
            addTokenToParameter(privateParameter);
        return super.subscribe(subscribeRequest);
    }

    @Override
    public <R extends AbstractChannelResult, P extends AbstractPublicationMessage, T extends ParameterInterface>
    List<Single<UnsubscribeResponse<R, P>>> unsubscribe(UnsubscribeRequest<T> unsubscribeRequest) {
        if (unsubscribeRequest.getParams() instanceof PrivateParameterInterface privateParameter)
            addTokenToParameter(privateParameter);
        return super.unsubscribe(unsubscribeRequest);
    }

    private void addTokenToParameter(PrivateParameterInterface param) {
        WebSocketsTokenResult tokenResponse = authenticationClient.getWebsocketsToken();
        param.setToken(tokenResponse.getToken());
    }
}
