package io.github.ngchinhow.kraken.websocket.client;

import io.github.ngchinhow.kraken.rest.client.MarketDataClient;

import static io.github.ngchinhow.kraken.properties.KrakenProperties.KRAKEN_WEBSOCKET_API_PUBLIC_URL;

public final class KrakenPublicWebSocketClient extends KrakenBaseWebSocketClient {
    public KrakenPublicWebSocketClient(MarketDataClient marketDataClient) {
        super(KRAKEN_WEBSOCKET_API_PUBLIC_URL, marketDataClient);
    }
}
