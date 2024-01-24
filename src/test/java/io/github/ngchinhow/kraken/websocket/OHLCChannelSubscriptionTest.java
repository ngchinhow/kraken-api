package io.github.ngchinhow.kraken.websocket;

import io.github.ngchinhow.kraken.manager.KrakenConnectionManager;
import io.github.ngchinhow.kraken.util.Helper;
import io.github.ngchinhow.kraken.websocket.client.KrakenPublicWebSocketClient;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.message.ohlc.OHLCMessage;
import io.github.ngchinhow.kraken.websocket.model.method.channel.ohlc.OHLCParameter;
import io.github.ngchinhow.kraken.websocket.model.method.channel.ohlc.OHLCResult;
import io.github.ngchinhow.kraken.websocket.model.method.subscription.SubscribeRequest;
import io.github.ngchinhow.kraken.websocket.model.method.subscription.SubscribeResponse;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.*;

class OHLCChannelSubscriptionTest {
    private KrakenPublicWebSocketClient client;

    @BeforeEach
    void beforeEach() throws InterruptedException {
        client = new KrakenConnectionManager(null, null).getKrakenPublicWebSocketClient();
        client.connectBlocking();
    }

    @Test
    void givenSubscription_thenSucceed() {
        SubscribeRequest<OHLCParameter> request = Helper.buildStandardOHLCSubscribeRequest();
        List<Single<SubscribeResponse<OHLCResult, OHLCMessage>>> responses = client.subscribe(request);

        responses.stream()
                 .map(Single::blockingGet)
                 .forEach(r -> {
                     assertThat(r)
                         .isNotNull()
                         .extracting(SubscribeResponse::getResult)
                         .isNotNull()
                         .doesNotReturn(null, from(OHLCResult::getInterval))
                         .doesNotReturn(null, from(OHLCResult::getSymbol));

                     ReplaySubject<OHLCMessage> publishSubject = r.getPublicationMessageReplaySubject();
                     try (Stream<OHLCMessage> stream = publishSubject.blockingStream()) {
                         stream.limit(5)
                               .filter(m -> m.getType().equals(ChannelMetadata.ChangeType.SNAPSHOT))
                               .forEach(System.out::println);
                     }
                 });
    }

    @Test
    void givenSubscriptionOnSameSymbol_thenFailOnSecondInterval() {
        SubscribeRequest<OHLCParameter> request = Helper.buildStandardOHLCSubscribeRequest();
        List<Single<SubscribeResponse<OHLCResult, OHLCMessage>>> responses = client.subscribe(request);
        assertNotNull(responses);

        // Change interval of request
        request.getParams().setInterval(1);

        List<Single<SubscribeResponse<OHLCResult, OHLCMessage>>> responsesNewInterval = client.subscribe(request);
        responsesNewInterval.stream()
                            .map(Single::blockingGet)
                            .forEach(r -> assertThat(r)
                                .returns(false, from(SubscribeResponse::getSuccess))
                                .returns("Already subscribed to one ohlc interval on this symbol",
                                         from(SubscribeResponse::getError)));
    }
}
