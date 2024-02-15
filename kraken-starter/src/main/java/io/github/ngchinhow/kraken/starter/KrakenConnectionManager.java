package io.github.ngchinhow.kraken.starter;

import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.rest.client.RestClient;
import io.github.ngchinhow.kraken.rest.client.WebSocketsAuthenticationClient;
import io.github.ngchinhow.kraken.rest.enums.RestEnumerations;
import io.github.ngchinhow.kraken.rest.factory.RestClientFactory;
import io.github.ngchinhow.kraken.websockets.client.PrivateWebSocketsClient;
import io.github.ngchinhow.kraken.websockets.client.PublicWebSocketsClient;

public final class KrakenConnectionManager {
    private final String apiKey;
    private final String privateKey;

    public KrakenConnectionManager(String apiKey, String privateKey) {
        this.apiKey = apiKey;
        this.privateKey = privateKey;
    }

    public <T extends RestClient> T getRestClient(Class<T> clazz) {
        return RestClientFactory.getPrivateRestClient(clazz, apiKey, privateKey);
    }

    public PrivateWebSocketsClient getKrakenPrivateWebSocketClient() {
        MarketDataClient marketDataClient = getRestClient(RestEnumerations.Endpoint.MARKET_DATA);
        WebSocketsAuthenticationClient webSocketsAuthenticationClient = getRestClient(RestEnumerations.Endpoint.WEBSOCKETS_AUTHENTICATION);
        return new PrivateWebSocketsClient(marketDataClient, webSocketsAuthenticationClient);
    }

    public PublicWebSocketsClient getKrakenPublicWebSocketClient() {
        MarketDataClient marketDataClient = getRestClient(RestEnumerations.Endpoint.MARKET_DATA);
        return new PublicWebSocketsClient(marketDataClient);
    }
}
