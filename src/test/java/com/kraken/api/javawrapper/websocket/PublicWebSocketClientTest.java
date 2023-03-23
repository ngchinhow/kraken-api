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
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.CHANNEL.BOOK;
import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.CHANNEL.OHLC;
import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT_TYPE.PONG;

public class PublicWebSocketClientTest {
    private KrakenPublicWebSocketClient publicWebSocketClient;

    @BeforeEach
    public void beforeEach() throws InterruptedException {
        publicWebSocketClient = new KrakenConnectionManager("", "").getKrakenPublicWebSocketClient();
        publicWebSocketClient.connectBlocking();
    }

    @Test
    public void givenPublicWebSocketClient_whenPing_thenPongWithSameReqId() {
        BigInteger reqId = new BigInteger("12987");
        PingMessage pingMessage = PingMessage.builder()
            .reqId(reqId)
            .build();
        PongMessage pongMessage = publicWebSocketClient.ping(pingMessage).blockingGet();
        Assertions.assertNotNull(pongMessage);
        Assertions.assertEquals(pongMessage.getEvent(), PONG);
        Assertions.assertEquals(pongMessage.getReqId(), reqId);
        Assertions.assertTrue(publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().isEmpty());
    }

    @Test
    public void givenPublicWebSocketClient_whenSubscribe_thenSuccess() {
        long testSize = 10L;
        int expectedDepth = 10;
        SubscribeMessage subscribeMessage = buildStandardSubscribeMessage();
        List<Single<SubscriptionStatusMessage>> list = publicWebSocketClient.subscribe(subscribeMessage);
        Assertions.assertEquals(
            2,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
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
                Assertions.assertEquals(expectedDepth, message.getSubscription().getDepth());
                String pair = message.getPair();
                PublishSubject<AbstractPublicationMessage> publishSubject = message.getPublicationMessagePublishSubject();
                Assertions.assertNotNull(publishSubject);
                publishSubject.blockingStream().limit(testSize).forEach(m -> {
                    BaseBookMessage baseBookMessage = (BaseBookMessage) m;
                    Assertions.assertEquals(pair, baseBookMessage.getPair());
                    Assertions.assertEquals(expectedDepth, baseBookMessage.getDepth());
                });
            });

        // Test subscribing using the same payload again
        subscribeMessage = buildStandardSubscribeMessage();
        List<Single<SubscriptionStatusMessage>> listResubscribe = publicWebSocketClient.subscribe(subscribeMessage);
        Assertions.assertEquals(
            2,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
        for (int i = 0; i < 2; i++) {
            SubscriptionStatusMessage original = list.get(i).blockingGet();
            SubscriptionStatusMessage duplicate = listResubscribe.get(i).blockingGet();
            Assertions.assertEquals(original, duplicate);
            String pair = duplicate.getPair();
            duplicate.getPublicationMessagePublishSubject().blockingStream().limit(testSize).forEach(m -> {
                BaseBookMessage baseBookMessage = (BaseBookMessage) m;
                Assertions.assertEquals(pair, baseBookMessage.getPair());
                Assertions.assertEquals(expectedDepth, baseBookMessage.getDepth());
            });
        }

        // Test changing reqId will not affect the SubscriptionStatusMessage
        subscribeMessage = buildStandardSubscribeMessage();
        subscribeMessage.setReqId(new BigInteger("123"));
        List<Single<SubscriptionStatusMessage>> listNewReqId = publicWebSocketClient.subscribe(subscribeMessage);
        Assertions.assertEquals(
            2,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
        for (int i = 0; i < 2; i++) {
            SubscriptionStatusMessage original = list.get(i).blockingGet();
            SubscriptionStatusMessage duplicate = listNewReqId.get(i).blockingGet();
            Assertions.assertEquals(original, duplicate);
            String pair = duplicate.getPair();
            duplicate.getPublicationMessagePublishSubject().blockingStream().limit(testSize).forEach(m -> {
                BaseBookMessage baseBookMessage = (BaseBookMessage) m;
                Assertions.assertEquals(pair, baseBookMessage.getPair());
                Assertions.assertEquals(expectedDepth, baseBookMessage.getDepth());
            });
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

    @Test
    public void test() {
        SubscribeMessage subscribeMessage = SubscribeMessage.builder()
            .pairs(List.of("XBT/USD", "XBT/EUR"))
            .reqId(new BigInteger("12345"))
            .subscription(SubscriptionEmbeddedObject.builder()
                .name(OHLC)
                .interval(1440)
                .build())
            .build();
        int i = 0;
        while (i < 10) {
            publicWebSocketClient.subscribe(subscribeMessage);
            i++;
        }
    }

    private SubscribeMessage buildStandardSubscribeMessage() {
        return SubscribeMessage.builder()
            .pairs(List.of("XBT/USD", "XBT/EUR"))
            .reqId(new BigInteger("12345"))
            .subscription(SubscriptionEmbeddedObject.builder()
                .name(BOOK)
                .depth(10)
                .build())
            .build();
    }
}
