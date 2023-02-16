package com.kraken.api.javawrapper.websocket;

import com.kraken.api.javawrapper.manager.KrakenConnectionManager;
import com.kraken.api.javawrapper.websocket.client.KrakenWebSocketClient;
import com.kraken.api.javawrapper.websocket.model.event.embedded.SubscriptionEmbeddedObject;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.internal.jdk8.CompletionStageConsumer;
import org.junit.jupiter.api.Test;

import java.util.List;

public class WebSocketClientTest {
    private static final KrakenWebSocketClient WEB_SOCKET_CLIENT = new KrakenConnectionManager("", "")
        .getKrakenWebSocketClient(
            List.of("XBT/USD", "XBT/EUR"),
            SubscriptionEmbeddedObject.builder()
                .name(WebSocketEnumerations.CHANNEL.BOOK)
                .depth(10)
                .build()
        );

    @Test
    public void givenWebSocketClient_whenSubscribePublic_thenSuccess() throws InterruptedException {
        WEB_SOCKET_CLIENT.connect();
        Thread.sleep(5000);
        WEB_SOCKET_CLIENT.ping().subscribe(System.out::println);
        while (true) {

        }
    }
}
