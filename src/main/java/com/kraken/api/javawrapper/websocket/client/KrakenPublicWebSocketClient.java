package com.kraken.api.javawrapper.websocket.client;

import com.kraken.api.javawrapper.rest.client.MarketDataClient;

import static com.kraken.api.javawrapper.properties.KrakenProperties.KRAKEN_WEBSOCKET_API_PUBLIC_URL;

public final class KrakenPublicWebSocketClient extends KrakenBaseWebSocketClient {
    public KrakenPublicWebSocketClient(MarketDataClient marketDataClient) {
        super(KRAKEN_WEBSOCKET_API_PUBLIC_URL, marketDataClient);
    }
}
