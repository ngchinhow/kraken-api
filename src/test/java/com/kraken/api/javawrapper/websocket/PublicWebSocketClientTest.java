package com.kraken.api.javawrapper.websocket;

import com.kraken.api.javawrapper.manager.KrakenConnectionManager;
import com.kraken.api.javawrapper.websocket.client.KrakenPublicWebSocketClient;
import com.kraken.api.javawrapper.websocket.enums.MethodMetadata;
import com.kraken.api.javawrapper.websocket.model.message.AbstractPublicationMessage;
import com.kraken.api.javawrapper.websocket.model.message.BookMessage;
import com.kraken.api.javawrapper.websocket.model.method.Echo;
import com.kraken.api.javawrapper.websocket.model.method.Subscription;
import com.kraken.api.javawrapper.websocket.model.method.detail.channel.BookSubscription;
import com.kraken.api.javawrapper.websocket.model.method.detail.channel.OHLCSubscription;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static com.kraken.api.javawrapper.websocket.enums.ChannelMetadata.ChannelType.BOOK;
import static com.kraken.api.javawrapper.websocket.enums.MethodMetadata.MethodType.PONG;

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
        Echo.PingRequest pingRequest = Echo.PingRequest.builder()
            .requestId(reqId)
            .build();
        Echo.PongResponse pongResponse = publicWebSocketClient.ping(pingRequest).blockingGet();
        Assertions.assertNotNull(pongResponse);
        Assertions.assertEquals(pongResponse.getMethod(), PONG);
        Assertions.assertEquals(pongResponse.getRequestId(), reqId);
        Assertions.assertTrue(publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().isEmpty());
    }

    @Test
    public void givenPublicWebSocketClient_whenSubscribe_thenSuccess() {
        long testSize = 10L;
        int expectedDepth = 10;
        Subscription.SubscribeRequest subscribeRequest = buildStandardSubscribeRequest();
        List<Single<Subscription.SubscribeResponse>> list = publicWebSocketClient.subscribe(subscribeRequest);
        Assertions.assertEquals(
            2,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
        list.stream()
            .map(Single::blockingGet)
            .forEach(message -> {
                Assertions.assertNotNull(message);
                Assertions.assertEquals(MethodMetadata.MethodType.SUBSCRIBE, message.getMethod());
                Assertions.assertEquals(subscribeRequest.getRequestId(), message.getRequestId());
                Assertions.assertTrue(message.getSuccess());
                Assertions.assertTrue(message.getResult() instanceof BookSubscription.Result);
                BookSubscription.Result result = (BookSubscription.Result) message.getResult();
                Assertions.assertEquals(BOOK, result.getChannel());
                Assertions.assertEquals(expectedDepth, result.getDepth());
                String symbol = result.getSymbol();
                PublishSubject<AbstractPublicationMessage> publishSubject = message.getPublicationMessagePublishSubject();
                Assertions.assertNotNull(publishSubject);
                publishSubject.blockingStream()
                    .limit(testSize)
                    .forEach(m -> {
                        BookMessage bookMessage = (BookMessage) m;
                        bookMessage.getData().forEach(book -> {
                            Assertions.assertEquals(symbol, book.getSymbol());
                            Assertions.assertEquals(expectedDepth, book.getAsks().size());
                            Assertions.assertEquals(expectedDepth, book.getBids().size());
                        });
                    });
            });

        // Test subscribing using the same payload again
//        subscribeRequest = buildStandardSubscribeRequest();
//        List<Single<SubscriptionStatusMessage>> listResubscribe = publicWebSocketClient.subscribe(subscribeRequest);
//        Assertions.assertEquals(
//            2,
//            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
//        );
//        Already subscribed
//        for (int i = 0; i < 2; i++) {
//            SubscriptionStatusMessage original = list.get(i).blockingGet();
//            SubscriptionStatusMessage duplicate = listResubscribe.get(i).blockingGet();
//            Assertions.assertEquals(original, duplicate);
//            String pair = duplicate.getPair();
//            duplicate.getPublicationMessagePublishSubject().blockingStream().limit(testSize).forEach(m -> {
//                BaseBookMessage baseBookMessage = (BaseBookMessage) m;
//                Assertions.assertEquals(pair, baseBookMessage.getPair());
//                Assertions.assertEquals(expectedDepth, baseBookMessage.getDepth());
//            });
//        }

        // Test changing reqId will not affect the SubscriptionStatusMessage
//        subscribeRequest = buildStandardSubscribeRequest();
//        subscribeRequest.setReqId(new BigInteger("123"));
//        List<Single<SubscriptionStatusMessage>> listNewReqId = publicWebSocketClient.subscribe(subscribeRequest);
//        Assertions.assertEquals(
//            2,
//            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
//        );
//        for (int i = 0; i < 2; i++) {
//            SubscriptionStatusMessage original = list.get(i).blockingGet();
//            SubscriptionStatusMessage duplicate = listNewReqId.get(i).blockingGet();
//            Assertions.assertEquals(original, duplicate);
//            String pair = duplicate.getPair();
//            duplicate.getPublicationMessagePublishSubject().blockingStream().limit(testSize).forEach(m -> {
//                BaseBookMessage baseBookMessage = (BaseBookMessage) m;
//                Assertions.assertEquals(pair, baseBookMessage.getPair());
//                Assertions.assertEquals(expectedDepth, baseBookMessage.getDepth());
//            });
//        }
    }

//    @Test
//    public void givenPublicWebSocketClient_whenSubscribeAndUnsubscribe_thenSuccess() {
//        SubscribeRequestMessage subscribeRequestMessage = buildStandardSubscribeMessage();
//        publicWebSocketClient.subscribe(subscribeRequestMessage).stream().map(Single::blockingGet).forEach(System.out::println);
//        UnsubscribeRequestMessage unsubscribeRequestMessage = UnsubscribeRequestMessage.builder()
//            .pairs(subscribeRequestMessage.getPairs())
//            .reqId(subscribeRequestMessage.getReqId())
//            .subscription(subscribeRequestMessage.getSubscription())
//            .build();
//        publicWebSocketClient.unsubscribe(unsubscribeRequestMessage);
//        while (true) {}

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
//    }

    @Test
    public void test() {
        Subscription.SubscribeRequest subscribeRequest = Subscription.SubscribeRequest.builder()
            .requestId(new BigInteger("12345"))
            .params(OHLCSubscription.Parameter.builder()
                .symbols(List.of("XBT/USD", "XBT/EUR"))
                .build())
            .build();
    }

    private Subscription.SubscribeRequest buildStandardSubscribeRequest() {
        return Subscription.SubscribeRequest.builder()
            .requestId(new BigInteger("12345"))
            .params(BookSubscription.Parameter.builder()
                .depth(10)
                .symbols(List.of("BTC/USD", "BTC/EUR"))
                .build())
            .build();
    }
}
