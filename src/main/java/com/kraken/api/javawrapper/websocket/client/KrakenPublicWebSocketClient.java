package com.kraken.api.javawrapper.websocket.client;

import static com.kraken.api.javawrapper.properties.KrakenProperties.KRAKEN_WEBSOCKET_API_PUBLIC_URL;

public class KrakenPublicWebSocketClient extends KrakenBaseWebSocketClient {
    public KrakenPublicWebSocketClient() {
        super(KRAKEN_WEBSOCKET_API_PUBLIC_URL);
    }
}
