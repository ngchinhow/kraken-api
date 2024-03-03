package io.github.ngchinhow.kraken.websockets;

import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.rest.factory.RestClientFactory;
import io.github.ngchinhow.kraken.websockets.client.PublicWebSocketsClient;
import io.github.ngchinhow.kraken.websockets.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.message.book.BookMessage;
import io.github.ngchinhow.kraken.websockets.model.message.instrument.Asset;
import io.github.ngchinhow.kraken.websockets.model.message.instrument.AssetPair;
import io.github.ngchinhow.kraken.websockets.model.message.instrument.Data;
import io.github.ngchinhow.kraken.websockets.model.message.instrument.InstrumentMessage;
import io.github.ngchinhow.kraken.websockets.model.message.ohlc.OHLCMessage;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class PublicWebSocketsClientTest {
    private PublicWebSocketsClient client;

    @BeforeEach
    void beforeEach() throws InterruptedException {
        final var marketDataClient = RestClientFactory.getPrivateRestClient(MarketDataClient.class, null, null);
        client = new PublicWebSocketsClient(marketDataClient);
        client.connectBlocking();
    }

    @AfterEach
    void afterEach() throws InterruptedException {
        client.closeBlocking();
    }

    @Test
    void givenPublicWebSocketsClient_whenPing_thenPongWithSameReqId() {
        BigInteger reqId = new BigInteger("12987");
        PingRequest pingRequest = PingRequest.builder()
                                             .requestId(reqId)
                                             .build();
        PongResponse pongResponseMessage = client.ping(pingRequest)
                                                 .blockingGet();

        assertThat(pongResponseMessage)
            .isNotNull()
            .returns(MethodMetadata.MethodType.PONG, from(PongResponse::getMethod))
            .returns(reqId, from(PongResponse::getRequestId));

        assertThat(client.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .isEmpty();
    }

    @Test
    void givenPublicWebSocketsClient_whenSubscribe_thenSucceed() {
        int testSize = 3;
        int expectedDepth = 10;
        SubscribeRequest<BookParameter> subscribeRequest = Helper.buildStandardBookSubscribeRequest();
        int numSymbols = subscribeRequest.getParams()
                                         .getSymbols()
                                         .size();
        List<Single<SubscribeResponse<BookResult, BookMessage>>> list = client.subscribe(subscribeRequest);

        assertThat(client.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
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
            listResubscribe = client.subscribe(subscribeRequest);

        assertThat(client.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
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
            listNewReqId = client.subscribe(subscribeRequest);

        assertThat(client.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
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
    void givenPublicWebSocketsClient_whenSubscribeUnknownSymbol_thenFail() {
        final var symbol = "UNKNOWN";
        SubscribeRequest<BookParameter> subscribeRequest = Helper.buildStandardBookSubscribeRequest();
        subscribeRequest.getParams().setSymbols(List.of(symbol));
        List<Single<SubscribeResponse<BookResult, BookMessage>>> list = client.subscribe(subscribeRequest);

        list.stream()
            .map(Single::blockingGet)
            .forEach(response -> assertThat(response)
                .isNotNull()
                .returns(subscribeRequest.getRequestId(), from(SubscribeResponse::getRequestId))
                .returns(false, from(SubscribeResponse::getSuccess))
                .returns("Currency pair not in ISO 4217-A3 format " + symbol, from(SubscribeResponse::getError)));
    }

    @Test
    void givenPublicWebSocketsClient_whenUnsubscribe_thenFail() {
        final var unsubscribeRequest = Helper.buildStandardBookUnsubscribeRequest();
        final var symbols = unsubscribeRequest.getParams()
                                              .getSymbols();
        List<Single<UnsubscribeResponse<BookResult>>> list = client.unsubscribe(unsubscribeRequest);

        assertThat(client.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .hasSize(symbols.size());

        List<String> responseSymbols = new ArrayList<>();
        list.stream()
            .map(Single::blockingGet)
            .forEach(response -> assertThat(response)
                .isNotNull()
                .returns(unsubscribeRequest.getRequestId(), from(UnsubscribeResponse::getRequestId))
                .returns(MethodMetadata.MethodType.UNSUBSCRIBE, from(UnsubscribeResponse::getMethod))
                .returns(false, from(UnsubscribeResponse::getSuccess))
                .returns("Subscription Not Found", from(UnsubscribeResponse::getError))
                .satisfies(r -> responseSymbols.add(r.getSymbol())));

        assertThat(responseSymbols)
            .isEqualTo(symbols);
    }

    @Test
    void givenPublicWebSocketsClient_whenSubscribeAndUnsubscribe_thenSucceed() {
        int expectedDepth = 10;
        SubscribeRequest<BookParameter> subscribeRequest = Helper.buildStandardBookSubscribeRequest();
        client.subscribe(subscribeRequest)
              .stream()
              .map(Single::blockingGet)
              .forEach(System.out::println);
        UnsubscribeRequest<BookParameter> unsubscribeRequest = Helper.buildStandardBookUnsubscribeRequest();
        int numSymbols = unsubscribeRequest.getParams()
                                           .getSymbols()
                                           .size();
        List<Single<UnsubscribeResponse<BookResult>>> list = client.unsubscribe(unsubscribeRequest);

        assertThat(client.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .hasSize(numSymbols);

        list.stream()
            .map(Single::blockingGet)
            .forEach(response -> assertThat(response)
                .isNotNull()
                .returns(MethodMetadata.MethodType.UNSUBSCRIBE, from(UnsubscribeResponse::getMethod))
                .returns(subscribeRequest.getRequestId(), from(UnsubscribeResponse::getRequestId))
                .returns(true, from(UnsubscribeResponse::getSuccess))
                .extracting(UnsubscribeResponse::getResult)
                .returns(ChannelMetadata.ChannelType.BOOK, from(BookResult::getChannel))
                .returns(expectedDepth, from(BookResult::getDepth)));
    }

    @Test
    void givenPublicWebSocketsClient_whenSubscribeTwoChannelSameReqId_thenSucceed() {
        int testSize = 3;
        SubscribeRequest<BookParameter> bookSubscribeRequest = Helper.buildStandardBookSubscribeRequest();
        SubscribeRequest<OHLCParameter> ohlcSubscribeRequest = Helper.buildStandardOHLCSubscribeRequest();
        List<Single<SubscribeResponse<BookResult, BookMessage>>>
            bookResponses = client.subscribe(bookSubscribeRequest);
        List<Single<SubscribeResponse<OHLCResult, OHLCMessage>>>
            ohlcResponses = client.subscribe(ohlcSubscribeRequest);

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
    void givenPublicWebSocketsClient_whenSubscribeChannelWithNoSymbol_thenSucceed() {
        int responseSize = 1;
        SubscribeRequest<InstrumentParameter>
            instrumentSubscribeRequest = Helper.buildStandardInstrumentSubscribeRequest();
        List<Single<SubscribeResponse<InstrumentResult, InstrumentMessage>>> responses =
            client.subscribe(instrumentSubscribeRequest);

        assertThat(responseSize)
            .isEqualTo(responses.size())
            .isEqualTo(client.getWebSocketsTrafficGateway().getRequestsToResponsesMap().size());

        Consumer<SubscribeResponse<InstrumentResult, InstrumentMessage>> instrumentConsumer = i -> assertThat(i)
            .extracting(SubscribeResponse::getResult)
            .isNotNull()
            .returns(true, from(InstrumentResult::getSnapshot));

        Consumer<Data> dataAssetsConsumer = d -> assertThat(d.getAssets())
            .extracting(Asset::getId)
            .doesNotContainNull();

        Consumer<Data> dataPairsConsumer = d -> assertThat(d.getPairs())
            .extracting(AssetPair::getSymbol)
            .doesNotContainNull();

        responses.stream()
                 .map(Single::blockingGet)
                 .forEach(r -> assertThat(r)
                     .isNotNull()
                     .returns(MethodMetadata.MethodType.SUBSCRIBE, from(SubscribeResponse::getMethod))
                     .returns(instrumentSubscribeRequest.getRequestId(), from(SubscribeResponse::getRequestId))
                     .returns(true, from(SubscribeResponse::getSuccess))
                     .satisfies(instrumentConsumer)
                     .extracting(SubscribeResponse::getPublicationMessageReplaySubject)
                     .isNotNull()
                     .extracting(ReplaySubject::blockingFirst)
                     .returns(ChannelMetadata.ChangeType.SNAPSHOT, InstrumentMessage::getType)
                     .extracting(InstrumentMessage::getData)
                     .satisfies(dataAssetsConsumer, dataPairsConsumer));
    }

    @Test
    void givenPublicWebSocketsClient_whenSubscribeLongerThan1Minute_thenPingToStayAlive() throws InterruptedException {
        var startTime = client.getClientOpenTime();
        SubscribeRequest<OHLCParameter> ohlcSubscribeRequest = Helper.buildStandardOHLCSubscribeRequest();
        client.subscribe(ohlcSubscribeRequest);

        // Wait for 1 minute
        TimeUnit.MINUTES.sleep(1);

        assertThat(client.getClientOpenTime())
            .isAfter(startTime);
    }
}
