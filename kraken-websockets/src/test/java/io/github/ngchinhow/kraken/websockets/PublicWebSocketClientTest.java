package io.github.ngchinhow.kraken.websockets;

import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.rest.factory.RestClientFactory;
import io.github.ngchinhow.kraken.websockets.client.PublicWebSocketsClient;
import io.github.ngchinhow.kraken.websockets.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.message.book.BookMessage;
import io.github.ngchinhow.kraken.websockets.model.message.instrument.Data;
import io.github.ngchinhow.kraken.websockets.model.message.instrument.InstrumentMessage;
import io.github.ngchinhow.kraken.websockets.model.message.ohlc.OHLCMessage;
import io.github.ngchinhow.kraken.websockets.model.message.instrument.Asset;
import io.github.ngchinhow.kraken.websockets.model.message.instrument.AssetPair;
import io.github.ngchinhow.kraken.websockets.model.method.channel.book.BookParameter;
import io.github.ngchinhow.kraken.websockets.model.method.channel.book.BookResult;
import io.github.ngchinhow.kraken.websockets.model.method.channel.instrument.InstrumentParameter;
import io.github.ngchinhow.kraken.websockets.model.method.channel.instrument.InstrumentResult;
import io.github.ngchinhow.kraken.websockets.model.method.channel.ohlc.OHLCParameter;
import io.github.ngchinhow.kraken.websockets.model.method.channel.ohlc.OHLCResult;
import io.github.ngchinhow.kraken.websockets.model.method.echo.PingRequest;
import io.github.ngchinhow.kraken.websockets.model.method.echo.PongResponse;
import io.github.ngchinhow.kraken.websockets.model.method.subscription.SubscribeRequest;
import io.github.ngchinhow.kraken.websockets.model.method.subscription.SubscribeResponse;
import io.github.ngchinhow.kraken.websockets.model.method.unsubscription.UnsubscribeRequest;
import io.github.ngchinhow.kraken.websockets.model.method.unsubscription.UnsubscribeResponse;
import io.github.ngchinhow.kraken.websockets.utils.Helper;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class PublicWebSocketClientTest {
    private PublicWebSocketsClient publicWebSocketClient;

    @BeforeEach
    void beforeEach() throws InterruptedException {
        final var marketDataClient = RestClientFactory.getRestClient(MarketDataClient.class, null, null);
        publicWebSocketClient = new PublicWebSocketsClient(marketDataClient);
        publicWebSocketClient.connectBlocking();
    }

    @Test
    void givenPublicWebSocketClient_whenPing_thenPongWithSameReqId() {
        BigInteger reqId = new BigInteger("12987");
        PingRequest pingRequest = PingRequest.builder()
                                             .requestId(reqId)
                                             .build();
        PongResponse pongResponseMessage = publicWebSocketClient.ping(pingRequest)
                                                                .blockingGet();

        assertThat(pongResponseMessage)
            .isNotNull()
            .returns(MethodMetadata.MethodType.PONG, from(PongResponse::getMethod))
            .returns(reqId, from(PongResponse::getRequestId));

        assertThat(publicWebSocketClient.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .isEmpty();
    }

    @Test
    void givenPublicWebSocketClient_whenSubscribe_thenSucceed() {
        int testSize = 10;
        int expectedDepth = 10;
        SubscribeRequest<BookParameter> subscribeRequest = Helper.buildStandardBookSubscribeRequest();
        int numSymbols = subscribeRequest.getParams()
                                         .getSymbols()
                                         .size();
        List<Single<SubscribeResponse<BookResult, BookMessage>>> list = publicWebSocketClient.subscribe(subscribeRequest);

        assertThat(publicWebSocketClient.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .hasSize(numSymbols);

        list.stream()
            .map(Single::blockingGet)
            .forEach(message -> {
                assertThat(message)
                    .isNotNull()
                    .returns(MethodMetadata.MethodType.SUBSCRIBE, from(SubscribeResponse::getMethod))
                    .returns(subscribeRequest.getRequestId(), from(SubscribeResponse::getRequestId))
                    .returns(true, from(SubscribeResponse::getSuccess))
                    .extracting(SubscribeResponse::getResult)
                    .isNotNull()
                    .returns(ChannelMetadata.ChannelType.BOOK, from(BookResult::getChannel))
                    .returns(expectedDepth, from(BookResult::getDepth));

                var symbol = message.getResult().getSymbol();
                ReplaySubject<BookMessage> publishSubject = message.getPublicationMessageReplaySubject();
                assertThat(publishSubject).isNotNull();
                try (Stream<BookMessage> stream = publishSubject.blockingStream()) {
                    stream.limit(testSize)
                          .forEach(m -> {
                              for (var book : m.getData()) {
                                  assertThat(book.getSymbol())
                                      .isEqualTo(symbol);

                                  if (m.getType() == ChannelMetadata.ChangeType.SNAPSHOT) {
                                      assertThat(book)
                                          .returns(expectedDepth, from(b -> b.getAsks().size()))
                                          .returns(expectedDepth, from(b -> b.getBids().size()));
                                  }
                              }
                          });
                }
            });

        // Test subscribing using the same payload again
        List<Single<SubscribeResponse<BookResult, BookMessage>>>
            listResubscribe = publicWebSocketClient.subscribe(subscribeRequest);

        assertThat(publicWebSocketClient.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .hasSize(numSymbols);

        for (int i = 0; i < numSymbols; i++) {
            SubscribeResponse<BookResult, BookMessage> duplicate = listResubscribe.get(i).blockingGet();
            assertThat(duplicate)
                .returns("Already subscribed", from(SubscribeResponse::getError))
                .returns(false, from(SubscribeResponse::getSuccess));

            SubscribeResponse<BookResult, BookMessage> original = list.get(i).blockingGet();
            assertThat(original)
                .returns(duplicate.getPublicationMessageReplaySubject(),
                         from(SubscribeResponse::getPublicationMessageReplaySubject));
        }

        // Test changing reqId will not affect the Publication PublishSubject
        subscribeRequest.setRequestId(new BigInteger("123"));
        List<Single<SubscribeResponse<BookResult, BookMessage>>>
            listNewReqId = publicWebSocketClient.subscribe(subscribeRequest);

        assertThat(publicWebSocketClient.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .hasSize(numSymbols);

        for (int i = 0; i < numSymbols; i++) {
            SubscribeResponse<BookResult, BookMessage> differentReqId = listNewReqId.get(i).blockingGet();
            assertThat(differentReqId)
                .returns("Already subscribed", from(SubscribeResponse::getError))
                .returns(false, from(SubscribeResponse::getSuccess));

            SubscribeResponse<BookResult, BookMessage> original = list.get(i).blockingGet();
            assertThat(original)
                .returns(differentReqId.getPublicationMessageReplaySubject(),
                         from(SubscribeResponse::getPublicationMessageReplaySubject));
        }
    }

    @Test
    void givenPublicWebSocketClient_whenUnsubscribe_thenFail() {
        UnsubscribeRequest<BookParameter> unsubscribeRequest = Helper.buildStandardBookUnsubscribeRequest();
        int numSymbols = unsubscribeRequest.getParams()
                                           .getSymbols()
                                           .size();
        List<Single<UnsubscribeResponse<BookResult, BookMessage>>>
            list = publicWebSocketClient.unsubscribe(unsubscribeRequest);

        assertThat(publicWebSocketClient.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .hasSize(numSymbols);

        list.stream()
            .map(Single::blockingGet)
            .forEach(response -> assertThat(response)
                .isNotNull()
                .returns(unsubscribeRequest.getRequestId(), from(UnsubscribeResponse::getRequestId))
                .returns(MethodMetadata.MethodType.UNSUBSCRIBE, from(UnsubscribeResponse::getMethod))
                .returns(false, from(UnsubscribeResponse::getSuccess))
                .returns("Subscription Not Found", from(UnsubscribeResponse::getError))
                .extracting(UnsubscribeResponse::getSymbol)
                .isNotNull());
    }

    @Test
    void givenPublicWebSocketClient_whenSubscribeAndUnsubscribe_thenSucceed() {
        int expectedDepth = 10;
        SubscribeRequest<BookParameter> subscribeRequest = Helper.buildStandardBookSubscribeRequest();
        publicWebSocketClient.subscribe(subscribeRequest)
                             .stream()
                             .map(Single::blockingGet)
                             .forEach(System.out::println);
        UnsubscribeRequest<BookParameter> unsubscribeRequest = Helper.buildStandardBookUnsubscribeRequest();
        int numSymbols = unsubscribeRequest.getParams()
                                           .getSymbols()
                                           .size();
        List<Single<UnsubscribeResponse<BookResult, BookMessage>>>
            list = publicWebSocketClient.unsubscribe(unsubscribeRequest);

        assertThat(publicWebSocketClient.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .hasSize(numSymbols);

        Predicate<UnsubscribeResponse<BookResult, BookMessage>> bookResultPredicate = r -> {
            assertThat(r)
                .extracting(UnsubscribeResponse::getResult)
                .returns(ChannelMetadata.ChannelType.BOOK, from(BookResult::getChannel))
                .returns(expectedDepth, from(BookResult::getDepth));
            return true;
        };

        Predicate<UnsubscribeResponse<BookResult, BookMessage>> publicationMessageReplaySubjectPredicate = r -> {
            assertThat(r)
                .extracting(UnsubscribeResponse::getPublicationMessageReplaySubject)
                .isNotNull()
                .returns(true, from(ReplaySubject::hasComplete));
            return true;
        };

        list.stream()
            .map(Single::blockingGet)
            .forEach(response -> assertThat(response)
                .isNotNull()
                .returns(MethodMetadata.MethodType.UNSUBSCRIBE, from(UnsubscribeResponse::getMethod))
                .returns(subscribeRequest.getRequestId(), from(UnsubscribeResponse::getRequestId))
                .returns(true, from(UnsubscribeResponse::getSuccess))
                .is(new Condition<>(bookResultPredicate, "book result"))
                .is(new Condition<>(publicationMessageReplaySubjectPredicate, "publication")));
    }

    @Test
    void givenPublicWebSocketClient_whenSubscribeTwoChannelSameReqId_thenSucceed() {
        int testSize = 10;
        SubscribeRequest<BookParameter> bookSubscribeRequest = Helper.buildStandardBookSubscribeRequest();
        SubscribeRequest<OHLCParameter> ohlcSubscribeRequest = Helper.buildStandardOHLCSubscribeRequest();
        List<Single<SubscribeResponse<BookResult, BookMessage>>>
            bookResponses = publicWebSocketClient.subscribe(bookSubscribeRequest);
        List<Single<SubscribeResponse<OHLCResult, OHLCMessage>>>
            ohlcResponses = publicWebSocketClient.subscribe(ohlcSubscribeRequest);

        bookResponses.stream()
                     .limit(testSize)
                     .map(Single::blockingGet)
                     .forEach(r -> assertThat(r)
                         .returns(true, from(SubscribeResponse::getSuccess)));
        ohlcResponses.stream()
                     .limit(testSize)
                     .map(Single::blockingGet)
                     .forEach(r -> assertThat(r)
                         .returns(true, from(SubscribeResponse::getSuccess)));
    }

    @Test
    void givenPublicWebSocketClient_whenSubscribeChannelWithNoSymbol_thenSucceed() {
        int responseSize = 1;
        SubscribeRequest<InstrumentParameter>
            instrumentSubscribeRequest = Helper.buildStandardInstrumentSubscribeRequest();
        List<Single<SubscribeResponse<InstrumentResult, InstrumentMessage>>> responses =
            publicWebSocketClient.subscribe(instrumentSubscribeRequest);

        assertThat(responseSize)
            .isEqualTo(responses.size())
            .isEqualTo(publicWebSocketClient.getWebSocketsTrafficGateway().getRequestsToResponsesMap().size());

        Predicate<SubscribeResponse<InstrumentResult, InstrumentMessage>> instrumentPredicate = i -> {
            assertThat(i)
                .extracting(SubscribeResponse::getResult)
                .isNotNull()
                .returns(true, from(InstrumentResult::getSnapshot));
            return true;
        };

        Predicate<Data> dataAssetsPredicate = d -> {
            assertThat(d.getAssets())
                .extracting(Asset::getId)
                .doesNotContainNull();
            return true;
        };

        Predicate<Data> dataPairsPredicate = d -> {
            assertThat(d.getPairs())
                .extracting(AssetPair::getSymbol)
                .doesNotContainNull();
            return true;
        };

        responses.stream()
                 .map(Single::blockingGet)
                 .forEach(r -> assertThat(r)
                     .isNotNull()
                     .returns(MethodMetadata.MethodType.SUBSCRIBE, from(SubscribeResponse::getMethod))
                     .returns(instrumentSubscribeRequest.getRequestId(), from(SubscribeResponse::getRequestId))
                     .returns(true, from(SubscribeResponse::getSuccess))
                     .is(new Condition<>(instrumentPredicate, "instrument result"))
                     .extracting(SubscribeResponse::getPublicationMessageReplaySubject)
                     .isNotNull()
                     .extracting(ReplaySubject::blockingFirst)
                     .returns(ChannelMetadata.ChangeType.SNAPSHOT, InstrumentMessage::getType)
                     .extracting(InstrumentMessage::getData)
                     .is(new Condition<>(dataAssetsPredicate, "data assets"))
                     .is(new Condition<>(dataPairsPredicate, "data pairs")));
    }

    @Test
    void givenPublicWebSocketClient_whenSubscribeLongerThan1Minute_thenPingToStayAlive() throws InterruptedException {
        var startTime = publicWebSocketClient.getClientOpenTime();
        SubscribeRequest<OHLCParameter> ohlcSubscribeRequest = Helper.buildStandardOHLCSubscribeRequest();
        publicWebSocketClient.subscribe(ohlcSubscribeRequest);

        // Wait for 1 minute
        TimeUnit.MINUTES.sleep(1);

        assertThat(publicWebSocketClient.getClientOpenTime())
            .isAfter(startTime);
    }
}
