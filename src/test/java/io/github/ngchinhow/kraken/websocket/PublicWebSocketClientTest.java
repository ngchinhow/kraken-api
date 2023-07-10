package io.github.ngchinhow.kraken.websocket;

import io.github.ngchinhow.kraken.manager.KrakenConnectionManager;
import io.github.ngchinhow.kraken.util.Helper;
import io.github.ngchinhow.kraken.websocket.client.KrakenPublicWebSocketClient;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websocket.model.message.book.BookMessage;
import io.github.ngchinhow.kraken.websocket.model.message.instrument.InstrumentMessage;
import io.github.ngchinhow.kraken.websocket.model.message.ohlc.OHLCMessage;
import io.github.ngchinhow.kraken.websocket.model.method.channel.book.BookParameter;
import io.github.ngchinhow.kraken.websocket.model.method.channel.book.BookResult;
import io.github.ngchinhow.kraken.websocket.model.method.channel.instrument.InstrumentParameter;
import io.github.ngchinhow.kraken.websocket.model.method.channel.instrument.InstrumentResult;
import io.github.ngchinhow.kraken.websocket.model.method.channel.ohlc.OHLCParameter;
import io.github.ngchinhow.kraken.websocket.model.method.channel.ohlc.OHLCResult;
import io.github.ngchinhow.kraken.websocket.model.method.echo.PingRequest;
import io.github.ngchinhow.kraken.websocket.model.method.echo.PongResponse;
import io.github.ngchinhow.kraken.websocket.model.method.subscription.SubscribeRequest;
import io.github.ngchinhow.kraken.websocket.model.method.subscription.SubscribeResponse;
import io.github.ngchinhow.kraken.websocket.model.method.unsubscription.UnsubscribeRequest;
import io.github.ngchinhow.kraken.websocket.model.method.unsubscription.UnsubscribeResponse;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(pongResponseMessage.getMethod(), MethodMetadata.MethodType.PONG);
        assertEquals(pongResponseMessage.getRequestId(), reqId);
        Assertions.assertTrue(publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().isEmpty());
    }

    @Test
    public void givenPublicWebSocketClient_whenSubscribe_thenSucceed() {
        int testSize = 10;
        int expectedDepth = 10;
        SubscribeRequest<BookParameter> subscribeRequest = Helper.buildStandardBookSubscribeRequest();
        int numSymbols = subscribeRequest.getParams().getSymbols().size();
        List<Single<SubscribeResponse<BookResult, BookMessage>>> list = publicWebSocketClient.subscribe(subscribeRequest);
        assertEquals(
            numSymbols,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
        list.stream()
            .map(Single::blockingGet)
            .forEach(message -> {
                Assertions.assertNotNull(message);
                assertEquals(MethodMetadata.MethodType.SUBSCRIBE, message.getMethod());
                assertEquals(subscribeRequest.getRequestId(), message.getRequestId());
                Assertions.assertTrue(message.getSuccess());
                BookResult result = message.getResult();
                Assertions.assertNotNull(result);
                assertEquals(ChannelMetadata.ChannelType.BOOK, result.getChannel());
                assertEquals(expectedDepth, result.getDepth());
                String symbol = result.getSymbol();
                ReplaySubject<BookMessage> publishSubject = message.getPublicationMessageReplaySubject();
                Assertions.assertNotNull(publishSubject);
                try (Stream<BookMessage> stream = publishSubject.blockingStream()) {
                    stream.limit(testSize).forEach(m -> m.getData().forEach(book -> {
                        assertEquals(symbol, book.getSymbol());
                        if (m.getType().equals(ChannelMetadata.ChangeType.SNAPSHOT)) {
                            assertEquals(expectedDepth, book.getAsks().size());
                            assertEquals(expectedDepth, book.getBids().size());
                        }
                    }));
                }
            });

        // Test subscribing using the same payload again
        List<Single<SubscribeResponse<BookResult, BookMessage>>> listResubscribe = publicWebSocketClient.subscribe(subscribeRequest);
        assertEquals(
            numSymbols,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
        for (int i = 0; i < numSymbols; i++) {
            SubscribeResponse<BookResult, BookMessage> duplicate = listResubscribe.get(i).blockingGet();
            assertEquals("Already subscribed", duplicate.getError());
            Assertions.assertFalse(duplicate.getSuccess());
            SubscribeResponse<BookResult, BookMessage> original = list.get(i).blockingGet();
            assertEquals(original.getPublicationMessageReplaySubject(), duplicate.getPublicationMessageReplaySubject());
        }

        // Test changing reqId will not affect the Publication PublishSubject
        subscribeRequest.setRequestId(new BigInteger("123"));
        List<Single<SubscribeResponse<BookResult, BookMessage>>> listNewReqId = publicWebSocketClient.subscribe(subscribeRequest);
        assertEquals(
            numSymbols,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
        for (int i = 0; i < 2; i++) {
            SubscribeResponse<BookResult, BookMessage> differentReqId = listNewReqId.get(i).blockingGet();
            assertEquals("Already subscribed", differentReqId.getError());
            Assertions.assertFalse(differentReqId.getSuccess());
            SubscribeResponse<BookResult, BookMessage> original = list.get(i).blockingGet();
            assertEquals(original.getPublicationMessageReplaySubject(), differentReqId.getPublicationMessageReplaySubject());
        }
    }

    @Test
    public void givenPublicWebSocketClient_whenSubscribeAndUnsubscribe_thenSucceed() {
        int expectedDepth = 10;
        SubscribeRequest<BookParameter> subscribeRequest = Helper.buildStandardBookSubscribeRequest();
        publicWebSocketClient.subscribe(subscribeRequest).stream().map(Single::blockingGet).forEach(System.out::println);
        UnsubscribeRequest<BookParameter> unsubscribeRequest = Helper.buildStandardBookUnsubscribeRequest();
        int numSymbols = unsubscribeRequest.getParams().getSymbols().size();
        List<Single<UnsubscribeResponse<BookResult, BookMessage>>> list = publicWebSocketClient.unsubscribe(unsubscribeRequest);
        assertEquals(
            numSymbols,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
        list.stream()
            .map(Single::blockingGet)
            .forEach(response -> {
                Assertions.assertNotNull(response);
                assertEquals(MethodMetadata.MethodType.UNSUBSCRIBE, response.getMethod());
                assertEquals(subscribeRequest.getRequestId(), response.getRequestId());
                Assertions.assertTrue(response.getSuccess());
                BookResult result = response.getResult();
                Assertions.assertNotNull(result);
                assertEquals(ChannelMetadata.ChannelType.BOOK, result.getChannel());
                assertEquals(expectedDepth, result.getDepth());
                ReplaySubject<BookMessage> publishSubject = response.getPublicationMessageReplaySubject();
                Assertions.assertNotNull(publishSubject);
                Assertions.assertTrue(publishSubject.hasComplete());
            });
    }

    @Test
    public void givenPublicWebSocketClient_whenSubscribeTwoChannelSameReqId_thenSucceed() {
        int testSize = 10;
        SubscribeRequest<BookParameter> bookSubscribeRequest = Helper.buildStandardBookSubscribeRequest();
        SubscribeRequest<OHLCParameter> ohlcSubscribeRequest = Helper.buildStandardOHLCSubscribeRequest();
        List<Single<SubscribeResponse<BookResult, BookMessage>>> bookResponses = publicWebSocketClient.subscribe(bookSubscribeRequest);
        List<Single<SubscribeResponse<OHLCResult, OHLCMessage>>> ohlcResponses = publicWebSocketClient.subscribe(ohlcSubscribeRequest);
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
        SubscribeRequest<InstrumentParameter> instrumentSubscribeRequest = Helper.buildStandardInstrumentSubscribeRequest();
        List<Single<SubscribeResponse<InstrumentResult, InstrumentMessage>>> responses = publicWebSocketClient
            .subscribe(instrumentSubscribeRequest);
        assertEquals(responseSize, responses.size());
        assertEquals(
            responseSize,
            publicWebSocketClient.getWebSocketTrafficGateway().getRequestsToResponsesMap().size()
        );
        responses.stream()
            .map(Single::blockingGet)
            .forEach(r -> {
                Assertions.assertNotNull(r);
                assertEquals(MethodMetadata.MethodType.SUBSCRIBE, r.getMethod());
                assertEquals(instrumentSubscribeRequest.getRequestId(), r.getRequestId());
                Assertions.assertTrue(r.getSuccess());
                InstrumentResult result = r.getResult();
                Assertions.assertNotNull(result);
                Assertions.assertTrue(result.getSnapshot());
                ReplaySubject<InstrumentMessage> publishSubject = r.getPublicationMessageReplaySubject();
                Assertions.assertNotNull(publishSubject);
                InstrumentMessage instrumentMessage = publishSubject.blockingFirst();
                assertEquals(ChannelMetadata.ChangeType.SNAPSHOT, instrumentMessage.getType());
                instrumentMessage.getData().getAssets().forEach(a -> Assertions.assertNotNull(a.getId()));
                instrumentMessage.getData().getPairs().forEach(a -> Assertions.assertNotNull(a.getSymbol()));
            });
    }
}
