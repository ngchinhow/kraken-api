package io.github.ngchinhow.kraken.websockets;

import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.rest.client.WebSocketsAuthenticationClient;
import io.github.ngchinhow.kraken.rest.factory.RestClientFactory;
import io.github.ngchinhow.kraken.websockets.client.PrivateWebSocketsClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

abstract class BasePrivateWebSocketsClientTest {

    protected static final String KRAKEN_REST_API_KEY = System.getenv("KRAKEN_REST_API_KEY");
    protected static final String KRAKEN_PRIVATE_API_KEY = System.getenv("KRAKEN_REST_PRIVATE_KEY");
    protected static final MarketDataClient MARKET_DATA_CLIENT = RestClientFactory.getPublicRestClient(MarketDataClient.class);
    private static final WebSocketsAuthenticationClient WEB_SOCKETS_AUTHENTICATION_CLIENT = RestClientFactory.getPrivateRestClient(
        WebSocketsAuthenticationClient.class,
        KRAKEN_REST_API_KEY,
        KRAKEN_PRIVATE_API_KEY);
    protected PrivateWebSocketsClient client;

    @BeforeEach
    void connect() throws InterruptedException {
        client = new PrivateWebSocketsClient(MARKET_DATA_CLIENT, WEB_SOCKETS_AUTHENTICATION_CLIENT);
        client.connectBlocking();
    }

    @AfterEach
    void close() throws InterruptedException {
        client.closeBlocking();
    }
}
