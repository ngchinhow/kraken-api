package com.kraken.api.javawrapper.websocket;

import com.kraken.api.javawrapper.manager.KrakenConnectionManager;
import com.kraken.api.javawrapper.websocket.client.KrakenPublicWebSocketClient;
import com.kraken.api.javawrapper.websocket.enums.ChannelMetadata;
import com.kraken.api.javawrapper.websocket.enums.MethodMetadata;
import com.kraken.api.javawrapper.websocket.model.message.AbstractPublicationMessage;
import com.kraken.api.javawrapper.websocket.model.message.BookMessage;
import com.kraken.api.javawrapper.websocket.model.method.Echo;
import com.kraken.api.javawrapper.websocket.model.method.Subscription;
import com.kraken.api.javawrapper.websocket.model.method.Unsubscription;
import com.kraken.api.javawrapper.websocket.model.method.detail.channel.BookSubscription;
import com.kraken.api.javawrapper.websocket.model.method.detail.channel.OHLCSubscription;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

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
        Echo.PongResponse pongResponseMessage = publicWebSocketClient.ping(pingRequest).blockingGet();
        Assertions.assertNotNull(pongResponseMessage);
        Assertions.assertEquals(pongResponseMessage.getMethod(), PONG);
        Assertions.assertEquals(pongResponseMessage.getRequestId(), reqId);
        Assertions.assertTrue(publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().isEmpty());
    }

    @Test
    public void givenPublicWebSocketClient_whenSubscribe_thenSuccess() {
        int testSize = 10;
        int expectedDepth = 10;
        Subscription.SubscribeRequest subscribeRequest = buildStandardBookSubscribeRequest();
        int numSymbols = subscribeRequest.getParams().getSymbols().size();
        List<Single<Subscription.SubscribeResponse>> list = publicWebSocketClient.subscribe(subscribeRequest);
        Assertions.assertEquals(
            numSymbols,
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
                try (Stream<AbstractPublicationMessage> stream = publishSubject.blockingStream()) {
                    stream.limit(testSize).forEach(m -> {
                        BookMessage bookMessage = (BookMessage) m;
                        bookMessage.getData().forEach(book -> {
                            Assertions.assertEquals(symbol, book.getSymbol());
                            if (m.getType().equals(ChannelMetadata.ChangeType.SNAPSHOT)) {
                                Assertions.assertEquals(expectedDepth, book.getAsks().size());
                                Assertions.assertEquals(expectedDepth, book.getBids().size());
                            }
                        });
                    });
                }
            });

        // Test subscribing using the same payload again
        List<Single<Subscription.SubscribeResponse>> listResubscribe = publicWebSocketClient.subscribe(subscribeRequest);
        Assertions.assertEquals(
            numSymbols,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
        for (int i = 0; i < numSymbols; i++) {
            Subscription.SubscribeResponse duplicate = listResubscribe.get(i).blockingGet();
            Assertions.assertEquals("Already subscribed", duplicate.getError());
            Assertions.assertFalse(duplicate.getSuccess());
            Subscription.SubscribeResponse original = list.get(i).blockingGet();
            Assertions.assertEquals(original.getPublicationMessagePublishSubject(), duplicate.getPublicationMessagePublishSubject());
        }

        // Test changing reqId will not affect the Publication PublishSubject
        subscribeRequest.setRequestId(new BigInteger("123"));
        List<Single<Subscription.SubscribeResponse>> listNewReqId = publicWebSocketClient.subscribe(subscribeRequest);
        Assertions.assertEquals(
            numSymbols,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
        for (int i = 0; i < 2; i++) {
            Subscription.SubscribeResponse differentReqId = listNewReqId.get(i).blockingGet();
            Assertions.assertEquals("Already subscribed", differentReqId.getError());
            Assertions.assertFalse(differentReqId.getSuccess());
            Subscription.SubscribeResponse original = list.get(i).blockingGet();
            Assertions.assertEquals(original.getPublicationMessagePublishSubject(), differentReqId.getPublicationMessagePublishSubject());
        }
    }

    @Test
    public void givenPublicWebSocketClient_whenSubscribeAndUnsubscribe_thenSuccess() {
        int expectedDepth = 10;
        Subscription.SubscribeRequest subscribeRequest = buildStandardBookSubscribeRequest();
        publicWebSocketClient.subscribe(subscribeRequest).stream().map(Single::blockingGet).forEach(System.out::println);
        Unsubscription.UnsubscribeRequest unsubscribeRequest = buildStandardBookUnsubscribeRequest();
        int numSymbols = unsubscribeRequest.getParams().getSymbols().size();
        List<Single<Unsubscription.UnsubscribeResponse>> list = publicWebSocketClient.unsubscribe(unsubscribeRequest);
        Assertions.assertEquals(
            numSymbols,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
        list.stream()
            .map(Single::blockingGet)
            .forEach(response -> {
                Assertions.assertNotNull(response);
                Assertions.assertEquals(MethodMetadata.MethodType.UNSUBSCRIBE, response.getMethod());
                Assertions.assertEquals(subscribeRequest.getRequestId(), response.getRequestId());
                Assertions.assertTrue(response.getSuccess());
                Assertions.assertTrue(response.getResult() instanceof BookSubscription.Result);
                BookSubscription.Result result = (BookSubscription.Result) response.getResult();
                Assertions.assertEquals(BOOK, result.getChannel());
                Assertions.assertEquals(expectedDepth, result.getDepth());
                PublishSubject<AbstractPublicationMessage> publishSubject = response.getPublicationMessagePublishSubject();
                Assertions.assertNotNull(publishSubject);
                Assertions.assertTrue(publishSubject.hasComplete());
            });
    }

    @Test
    public void givenPublicWebSocketClient_whenSubscribeTwoChannelSameReqId_thenFail() {
        Subscription.SubscribeRequest bookSubscribeRequest = this.buildStandardBookSubscribeRequest();
        Subscription.SubscribeRequest ohlcSubscribeRequest = this.buildStandardOHLCSubscribeRequest();
        List<Single<Subscription.SubscribeResponse>> bookResponses = publicWebSocketClient.subscribe(bookSubscribeRequest);
        List<Single<Subscription.SubscribeResponse>> ohlcResponses = publicWebSocketClient.subscribe(ohlcSubscribeRequest);
        for (int i = 0; i < 2; i++) {
            Subscription.SubscribeResponse bookResponse = bookResponses.get(i).blockingGet();
            Subscription.SubscribeResponse ohlcResponse = ohlcResponses.get(i).blockingGet();
        }
    }

    @Test
    public void test() {
        Subscription.SubscribeRequest subscribeRequest = Subscription.SubscribeRequest.builder()
            .requestId(new BigInteger("12345"))
            .params(OHLCSubscription.Parameter.builder()
                .symbols(List.of("XBT/USD", "XBT/EUR"))
                .build())
            .build();
    }

    private Subscription.SubscribeRequest buildStandardBookSubscribeRequest() {
        return Subscription.SubscribeRequest.builder()
            .requestId(new BigInteger("12345"))
            .params(BookSubscription.Parameter.builder()
                .depth(10)
                .symbols(List.of("BTC/USD", "BTC/EUR"))
                .build())
            .build();
    }

    private Unsubscription.UnsubscribeRequest buildStandardBookUnsubscribeRequest() {
        return Unsubscription.UnsubscribeRequest.builder()
            .requestId(new BigInteger("12345"))
            .params(BookSubscription.Parameter.builder()
                .depth(10)
                .symbols(List.of("BTC/USD", "BTC/EUR"))
                .build())
            .build();
    }

    private Subscription.SubscribeRequest buildStandardOHLCSubscribeRequest() {
        return Subscription.SubscribeRequest.builder()
            .requestId(new BigInteger("12345"))
            .params(OHLCSubscription.Parameter.builder()
                .interval(30)
                .symbols(List.of("BTC/USD", "BTC/EUR"))
                .build())
            .build();
    }
}
