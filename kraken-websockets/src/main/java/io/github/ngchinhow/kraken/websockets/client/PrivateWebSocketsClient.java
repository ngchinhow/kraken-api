package io.github.ngchinhow.kraken.websockets.client;

import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.rest.client.WebSocketsAuthenticationClient;
import io.github.ngchinhow.kraken.rest.model.websocketsauthentication.token.WebSocketsTokenResult;
import io.github.ngchinhow.kraken.websockets.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websockets.model.method.PrivateParameterInterface;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelParameter;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelResult;
import io.github.ngchinhow.kraken.websockets.model.method.subscription.SubscribeRequest;
import io.github.ngchinhow.kraken.websockets.model.method.subscription.SubscribeResponse;
import io.github.ngchinhow.kraken.websockets.model.method.unsubscription.UnsubscribeRequest;
import io.github.ngchinhow.kraken.websockets.model.method.unsubscription.UnsubscribeResponse;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static io.github.ngchinhow.kraken.websockets.properties.WebSocketsProperties.PRIVATE_API_URL;

@Slf4j
public final class PrivateWebSocketsClient extends BaseWebSocketsClient {
    private final WebSocketsAuthenticationClient authenticationClient;

    public PrivateWebSocketsClient(MarketDataClient marketDataClient, WebSocketsAuthenticationClient authenticationClient) {
        super(PRIVATE_API_URL, marketDataClient);
        this.authenticationClient = authenticationClient;
    }

    public <R extends AbstractChannelResult, P extends AbstractPublicationMessage, T extends AbstractChannelParameter>
    List<Single<SubscribeResponse<R, P>>> subscribe(SubscribeRequest<T> subscribeRequest) {
        if (subscribeRequest.getParams() instanceof PrivateParameterInterface privateParameter)
            addTokenToParameter(privateParameter);
        return super.subscribe(subscribeRequest);
    }

    @Override
    public <R extends AbstractChannelResult, P extends AbstractPublicationMessage, T extends AbstractChannelParameter>
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
