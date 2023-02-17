package com.kraken.api.javawrapper.websocket;

import com.kraken.api.javawrapper.manager.KrakenConnectionManager;
import com.kraken.api.javawrapper.websocket.client.KrakenPublicWebSocketClient;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import com.kraken.api.javawrapper.websocket.model.event.embedded.SubscriptionEmbeddedObject;
import com.kraken.api.javawrapper.websocket.model.event.request.SubscribeMessage;
import org.junit.jupiter.api.Test;

import java.util.List;

public class WebSocketClientTest {
    private static final KrakenPublicWebSocketClient WEB_SOCKET_CLIENT = new KrakenConnectionManager("", "")
        .getKrakenPublicWebSocketClient();

    @Test
    public void givenWebSocketClient_whenSubscribePublic_thenSuccess() throws InterruptedException {
        WEB_SOCKET_CLIENT.connectBlocking();
        WEB_SOCKET_CLIENT.ping().toCompletionStage().thenAccept(System.out::println);
        WEB_SOCKET_CLIENT.subscribe(SubscribeMessage.builder()
                .pair(List.of("XBT/USD", "XBT/EUR"))
                .subscription(SubscriptionEmbeddedObject.builder()
                    .name(WebSocketEnumerations.CHANNEL.BOOK)
                    .depth(10)
                    .build())
                .build())
            .forEach(o -> {
                o.getSubscriptionStatusMessage().toCompletionStage().thenAccept(System.out::println);
                o.getPublicationMessageReplaySubject().forEach(System.out::println);
            });
        while (true) {

        }
//        this.send(subscribeAsJson);
//        UnsubscribeMessage unsubscribeMessage = UnsubscribeMessage.builder()
//            .subscription(subscriptionEmbeddedObject)
//            .pair(pairs)
//            .build();
//        String unsubscribeAsJson;
//        try {
//            unsubscribeAsJson = objectMapper.writeValueAsString(unsubscribeMessage);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        log.info("Unsubscription payload: {}", unsubscribeAsJson);
//        this.send(unsubscribeAsJson);
    }
}
