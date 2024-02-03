package io.github.ngchinhow.kraken.websockets.client;

import io.github.ngchinhow.kraken.rest.client.MarketDataClient;

import static io.github.ngchinhow.kraken.websockets.properties.WebSocketsProperties.PUBLIC_API_URL;

public final class PublicWebSocketsClient extends BaseWebSocketsClient {
    public PublicWebSocketsClient(MarketDataClient marketDataClient) {
        super(PUBLIC_API_URL, marketDataClient);
    }
}
