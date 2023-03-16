package com.kraken.api.javawrapper.websocket;

import com.kraken.api.javawrapper.manager.KrakenConnectionManager;
import com.kraken.api.javawrapper.websocket.client.KrakenPublicWebSocketClient;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import com.kraken.api.javawrapper.websocket.model.event.embedded.SubscriptionEmbeddedObject;
import com.kraken.api.javawrapper.websocket.model.event.request.PingMessage;
import com.kraken.api.javawrapper.websocket.model.event.request.SubscribeMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.PongMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.SubscriptionStatusMessage;
import com.kraken.api.javawrapper.websocket.model.publication.AbstractPublicationMessage;
import com.kraken.api.javawrapper.websocket.model.publication.BaseBookMessage;
import com.kraken.api.javawrapper.websocket.model.publication.BookSnapshotMessage;
import com.kraken.api.javawrapper.websocket.model.publication.BookUpdateMessage;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.CHANNEL.BOOK;
import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT_TYPE.PONG;

public class WebSocketClientTest {
    private KrakenPublicWebSocketClient webSocketClient;

    @BeforeEach
    public void beforeEach() throws InterruptedException {
        webSocketClient = new KrakenConnectionManager("", "").getKrakenPublicWebSocketClient();
        webSocketClient.connectBlocking();
    }

    @Test
    public void givenPublicWebSocketClient_whenPing_thenPongWithSameReqId() {
        BigInteger reqId = new BigInteger("12987");
        PingMessage pingMessage = PingMessage.builder()
            .reqId(reqId)
            .build();
        PongMessage pongMessage = webSocketClient.ping(pingMessage).blockingGet();
        Assertions.assertNotNull(pongMessage);
        Assertions.assertEquals(pongMessage.getEvent(), PONG);
        Assertions.assertEquals(pongMessage.getReqId(), reqId);
    }

    @Test
    public void givenPublicWebSocketClient_whenSubscribe_thenSuccess() {
        SubscribeMessage subscribeMessage = SubscribeMessage.builder()
            .pair(List.of("XBT/USD", "XBT/EUR"))
            .subscription(SubscriptionEmbeddedObject.builder()
                .name(BOOK)
                .depth(10)
                .build())
            .build();
        List<Single<SubscriptionStatusMessage>> list = webSocketClient.subscribe(subscribeMessage);
        list.stream()
            .map(Single::blockingGet)
            .forEach(message -> {
                Assertions.assertNotNull(message);
                Assertions.assertEquals(WebSocketEnumerations.EVENT_TYPE.SUBSCRIPTION_STATUS, message.getEvent());
                Assertions.assertEquals(BOOK, message.getSubscription().getName());
                Assertions.assertEquals(
                    WebSocketEnumerations.SUBSCRIPTION_STATUS_TYPE.SUBSCRIBED,
                    message.getStatus()
                );
                Assertions.assertEquals(10, message.getSubscription().getDepth());
                String pair = message.getPair();
                ReplaySubject<AbstractPublicationMessage> replaySubject = message.getPublicationMessageReplaySubject();
                Assertions.assertNotNull(replaySubject);
                Iterator<AbstractPublicationMessage> publicationMessageIterable = replaySubject.blockingIterable().iterator();
                BaseBookMessage baseBookMessage = (BaseBookMessage) publicationMessageIterable.next();
                Assertions.assertTrue(baseBookMessage instanceof BookSnapshotMessage);
                Assertions.assertTrue(baseBookMessage.isSnapshot());
                Assertions.assertEquals(pair, baseBookMessage.getPair());
                Assertions.assertEquals(10, baseBookMessage.getDepth());
                baseBookMessage = (BaseBookMessage) publicationMessageIterable.next();
                Assertions.assertTrue(baseBookMessage instanceof BookUpdateMessage);
                Assertions.assertFalse(baseBookMessage.isSnapshot());
                Assertions.assertEquals(pair, baseBookMessage.getPair());
                Assertions.assertEquals(10, baseBookMessage.getDepth());
            });

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
