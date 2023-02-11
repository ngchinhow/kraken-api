package com.kraken.api.javawrapper.websocket;

import com.kraken.api.javawrapper.manager.KrakenConnectionManager;
import com.kraken.api.javawrapper.websocket.client.KrakenWebSocketClient;
import com.kraken.api.javawrapper.websocket.dto.general.SubscriptionEmbeddedObject;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import org.junit.jupiter.api.Test;

public class WebSocketClientTest {
    private static final KrakenWebSocketClient WEB_SOCKET_CLIENT = new KrakenConnectionManager("", "")
        .getKrakenWebSocketClient(
            "XBT/USD",
            SubscriptionEmbeddedObject.builder()
                .name(WebSocketEnumerations.CHANNEL.BOOK)
                .depth(10)
                .build()
        );

    @Test
    public void givenWebSocketClient_whenSubscribePublic_thenSuccess() throws InterruptedException {
        WEB_SOCKET_CLIENT.connect();
//        while (true) {
//
//        }
    }
}
