package io.github.ngchinhow.kraken.websocket;

import io.github.ngchinhow.kraken.manager.KrakenConnectionManager;
import io.github.ngchinhow.kraken.websocket.client.KrakenPublicWebSocketClient;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websocket.model.message.book.BookMessage;
import io.github.ngchinhow.kraken.websocket.model.message.instrument.InstrumentMessage;
import io.github.ngchinhow.kraken.websocket.model.method.channel.book.BookParameter;
import io.github.ngchinhow.kraken.websocket.model.method.channel.book.BookResult;
import io.github.ngchinhow.kraken.websocket.model.method.channel.instrument.InstrumentParameter;
import io.github.ngchinhow.kraken.websocket.model.method.channel.instrument.InstrumentResult;
import io.github.ngchinhow.kraken.websocket.model.method.channel.ohlc.OHLCParameter;
import io.github.ngchinhow.kraken.websocket.model.method.echo.PingRequest;
import io.github.ngchinhow.kraken.websocket.model.method.echo.PongResponse;
import io.github.ngchinhow.kraken.websocket.model.method.subscription.SubscribeRequest;
import io.github.ngchinhow.kraken.websocket.model.method.subscription.SubscribeResponse;
import io.github.ngchinhow.kraken.websocket.model.method.unsubscription.UnsubscribeRequest;
import io.github.ngchinhow.kraken.websocket.model.method.unsubscription.UnsubscribeResponse;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

public class PublicWebSocketClientTest {
    private KrakenPublicWebSocketClient publicWebSocketClient;

    @BeforeEach
    public void beforeEach() throws InterruptedException {
        publicWebSocketClient = new KrakenConnectionManager(null, null).getKrakenPublicWebSocketClient();
        publicWebSocketClient.connectBlocking();
    }

    @Test
    public void givenPublicWebSocketClient_whenPing_thenPongWithSameReqId() {
        BigInteger reqId = new BigInteger("12987");
        PingRequest pingRequest = PingRequest.builder()
            .requestId(reqId)
            .build();
        PongResponse pongResponseMessage = publicWebSocketClient.ping(pingRequest).blockingGet();
        Assertions.assertNotNull(pongResponseMessage);
        Assertions.assertEquals(pongResponseMessage.getMethod(), MethodMetadata.MethodType.PONG);
        Assertions.assertEquals(pongResponseMessage.getRequestId(), reqId);
        Assertions.assertTrue(publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().isEmpty());
    }

    @Test
    public void givenPublicWebSocketClient_whenSubscribe_thenSucceed() {
        int testSize = 10;
        int expectedDepth = 10;
        SubscribeRequest subscribeRequest = buildStandardBookSubscribeRequest();
        int numSymbols = subscribeRequest.getParams().getSymbols().size();
        List<Single<SubscribeResponse>> list = publicWebSocketClient.subscribe(subscribeRequest);
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
                Assertions.assertTrue(message.getResult() instanceof BookResult);
                BookResult result = (BookResult) message.getResult();
                Assertions.assertEquals(ChannelMetadata.ChannelType.BOOK, result.getChannel());
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
        List<Single<SubscribeResponse>> listResubscribe = publicWebSocketClient.subscribe(subscribeRequest);
        Assertions.assertEquals(
            numSymbols,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
        for (int i = 0; i < numSymbols; i++) {
            SubscribeResponse duplicate = listResubscribe.get(i).blockingGet();
            Assertions.assertEquals("Already subscribed", duplicate.getError());
            Assertions.assertFalse(duplicate.getSuccess());
            SubscribeResponse original = list.get(i).blockingGet();
            Assertions.assertEquals(original.getPublicationMessagePublishSubject(), duplicate.getPublicationMessagePublishSubject());
        }

        // Test changing reqId will not affect the Publication PublishSubject
        subscribeRequest.setRequestId(new BigInteger("123"));
        List<Single<SubscribeResponse>> listNewReqId = publicWebSocketClient.subscribe(subscribeRequest);
        Assertions.assertEquals(
            numSymbols,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
        for (int i = 0; i < 2; i++) {
            SubscribeResponse differentReqId = listNewReqId.get(i).blockingGet();
            Assertions.assertEquals("Already subscribed", differentReqId.getError());
            Assertions.assertFalse(differentReqId.getSuccess());
            SubscribeResponse original = list.get(i).blockingGet();
            Assertions.assertEquals(original.getPublicationMessagePublishSubject(), differentReqId.getPublicationMessagePublishSubject());
        }
    }

    @Test
    public void givenPublicWebSocketClient_whenSubscribeAndUnsubscribe_thenSucceed() {
        int expectedDepth = 10;
        SubscribeRequest subscribeRequest = buildStandardBookSubscribeRequest();
        publicWebSocketClient.subscribe(subscribeRequest).stream().map(Single::blockingGet).forEach(System.out::println);
        UnsubscribeRequest unsubscribeRequest = buildStandardBookUnsubscribeRequest();
        int numSymbols = unsubscribeRequest.getParams().getSymbols().size();
        List<Single<UnsubscribeResponse>> list = publicWebSocketClient.unsubscribe(unsubscribeRequest);
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
                Assertions.assertTrue(response.getResult() instanceof BookResult);
                BookResult result = (BookResult) response.getResult();
                Assertions.assertEquals(ChannelMetadata.ChannelType.BOOK, result.getChannel());
                Assertions.assertEquals(expectedDepth, result.getDepth());
                PublishSubject<AbstractPublicationMessage> publishSubject = response.getPublicationMessagePublishSubject();
                Assertions.assertNotNull(publishSubject);
                Assertions.assertTrue(publishSubject.hasComplete());
            });
    }

    @Test
    public void givenPublicWebSocketClient_whenSubscribeTwoChannelSameReqId_thenSucceed() {
        int testSize = 10;
        SubscribeRequest bookSubscribeRequest = this.buildStandardBookSubscribeRequest();
        SubscribeRequest ohlcSubscribeRequest = this.buildStandardOHLCSubscribeRequest();
        List<Single<SubscribeResponse>> bookResponses = publicWebSocketClient.subscribe(bookSubscribeRequest);
        List<Single<SubscribeResponse>> ohlcResponses = publicWebSocketClient.subscribe(ohlcSubscribeRequest);
        bookResponses.stream()
            .limit(testSize)
            .map(Single::blockingGet)
            .forEach(r -> Assertions.assertTrue(r.getSuccess()));
        ohlcResponses.stream()
            .limit(testSize)
            .map(Single::blockingGet)
            .forEach(r -> Assertions.assertTrue(r.getSuccess()));
    }

    @Test
    public void givenPublicWebSocketClient_whenSubscribeChannelWithNoSymbol_thenSucceed() {
        int responseSize = 1;
        SubscribeRequest instrumentSubscribeRequest = buildStandardInstrumentSubscribeRequest();
        List<Single<SubscribeResponse>> responses = publicWebSocketClient.subscribe(instrumentSubscribeRequest);
        Assertions.assertEquals(responseSize, responses.size());
        Assertions.assertEquals(
            responseSize,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
        responses.stream()
            .map(Single::blockingGet)
            .forEach(r -> {
                Assertions.assertNotNull(r);
                Assertions.assertEquals(MethodMetadata.MethodType.SUBSCRIBE, r.getMethod());
                Assertions.assertEquals(instrumentSubscribeRequest.getRequestId(), r.getRequestId());
                Assertions.assertTrue(r.getSuccess());
                Assertions.assertTrue(r.getResult() instanceof InstrumentResult);
                InstrumentResult result = (InstrumentResult) r.getResult();
                Assertions.assertTrue(result.getSnapshot());
                PublishSubject<AbstractPublicationMessage> publishSubject = r.getPublicationMessagePublishSubject();
                Assertions.assertNotNull(publishSubject);
                AbstractPublicationMessage message = publishSubject.blockingFirst();
                Assertions.assertTrue(message instanceof InstrumentMessage);
                InstrumentMessage instrumentMessage = (InstrumentMessage) message;
                Assertions.assertEquals(ChannelMetadata.ChangeType.SNAPSHOT, instrumentMessage.getType());
                instrumentMessage.getData().getAssets().forEach(a -> Assertions.assertNotNull(a.getId()));
                instrumentMessage.getData().getPairs().forEach(a -> Assertions.assertNotNull(a.getSymbol()));
            });
    }

    private SubscribeRequest buildStandardBookSubscribeRequest() {
        return SubscribeRequest.builder()
            .requestId(new BigInteger("12345"))
            .params(BookParameter.builder()
                .depth(10)
                .symbols(List.of("BTC/USD", "BTC/EUR"))
                .build())
            .build();
    }

    private UnsubscribeRequest buildStandardBookUnsubscribeRequest() {
        return UnsubscribeRequest.builder()
            .requestId(new BigInteger("12345"))
            .params(BookParameter.builder()
                .depth(10)
                .symbols(List.of("BTC/USD", "BTC/EUR"))
                .build())
            .build();
    }

    private SubscribeRequest buildStandardOHLCSubscribeRequest() {
        return SubscribeRequest.builder()
            .requestId(new BigInteger("12345"))
            .params(OHLCParameter.builder()
                .interval(30)
                .symbols(List.of("BTC/USD", "BTC/EUR"))
                .build())
            .build();
    }

    private SubscribeRequest buildStandardInstrumentSubscribeRequest() {
        return SubscribeRequest.builder()
            .requestId(new BigInteger("12345"))
            .params(InstrumentParameter.builder()
                .snapshot(true)
                .build())
            .build();
    }
}
