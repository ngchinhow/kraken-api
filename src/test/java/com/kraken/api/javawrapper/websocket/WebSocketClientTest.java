package com.kraken.api.javawrapper.websocket;

import com.kraken.api.javawrapper.manager.KrakenConnectionManager;
import com.kraken.api.javawrapper.websocket.client.KrakenPublicWebSocketClient;
import com.kraken.api.javawrapper.websocket.model.event.request.PingMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT_TYPE.PONG;

public class WebSocketClientTest {
    private static final KrakenPublicWebSocketClient WEB_SOCKET_CLIENT = new KrakenConnectionManager("", "")
        .getKrakenPublicWebSocketClient();

    @BeforeEach
    public void beforeEach() throws InterruptedException {
        WEB_SOCKET_CLIENT.connectBlocking();
    }

    @Test
    public void givenPublicWebSocketClient_whenPing_thenPongWithSameReqId() {
        BigInteger reqId = new BigInteger("12987");
        PingMessage pingMessage = PingMessage.builder()
            .reqId(reqId)
            .build();
        WEB_SOCKET_CLIENT.ping(pingMessage).toCompletionStage().thenAccept(m -> {
            Assertions.assertNotNull(m);
            Assertions.assertEquals(m.getEvent(), PONG);
            Assertions.assertEquals(m.getReqId(), reqId);
        });
    }

    @Test
    public void givenPublicWebSocketClient_whenSubscribe_thenSuccess() {
//        SubscribeMessage subscribeMessage = new SubscribeMessage().toRequestIdentifier()
//            .pair(List.of("XBT/USD", "XBT/EUR"))
//            .subscription(SubscriptionEmbeddedObject.builder()
//                .name(WebSocketEnumerations.CHANNEL.BOOK)
//                .depth(10)
//                .build())
//            .build()
//        WEB_SOCKET_CLIENT.subscribe()
//            .forEach(o -> {
//                o.getSubscriptionStatusMessage().toCompletionStage().thenAccept(System.out::println);
//                o.getPublicationMessageReplaySubject().forEach(System.out::println);
//            });
//        while (true) {
//
//        }
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
