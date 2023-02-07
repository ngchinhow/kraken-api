package org.trading.krakenapi.websocket;

import com.kraken.api.javawrapper.manager.KrakenConnectionManager;
import com.kraken.api.javawrapper.websocket.client.KrakenWebSocketClient;
import com.kraken.api.javawrapper.websocket.dto.general.SubscriptionEmbeddedObject;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;

public class WebsocketConnectionManagerTest {

    public void givenCredentials_whenGettingConnectionManager_thenSuccess() {
        KrakenConnectionManager krakenConnectionManager = new KrakenConnectionManager("", "");
        SubscriptionEmbeddedObject subscription = SubscriptionEmbeddedObject.builder()
            .name(WebSocketEnumerations.CHANNEL.TICKER)
            .build();
        KrakenWebSocketClient webSocketClient = krakenConnectionManager.getKrakenWebSocketClient("XBT/USD", subscription);
        webSocketClient.connect();
    }
}
