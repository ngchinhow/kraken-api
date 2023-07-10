package io.github.ngchinhow.kraken.util;

import io.github.ngchinhow.kraken.websocket.model.method.channel.book.BookParameter;
import io.github.ngchinhow.kraken.websocket.model.method.channel.instrument.InstrumentParameter;
import io.github.ngchinhow.kraken.websocket.model.method.channel.ohlc.OHLCParameter;
import io.github.ngchinhow.kraken.websocket.model.method.subscription.SubscribeRequest;
import io.github.ngchinhow.kraken.websocket.model.method.unsubscription.UnsubscribeRequest;

import java.math.BigInteger;
import java.util.List;

public abstract class Helper {

    public static SubscribeRequest<BookParameter> buildStandardBookSubscribeRequest() {
        return SubscribeRequest.<BookParameter>builder()
            .requestId(new BigInteger("12345"))
            .params(BookParameter.builder()
                .depth(10)
                .symbols(List.of("BTC/USD", "BTC/EUR"))
                .build())
            .build();
    }

    public static UnsubscribeRequest<BookParameter> buildStandardBookUnsubscribeRequest() {
        return UnsubscribeRequest.<BookParameter>builder()
            .requestId(new BigInteger("12345"))
            .params(BookParameter.builder()
                .depth(10)
                .symbols(List.of("BTC/USD", "BTC/EUR"))
                .build())
            .build();
    }

    public static SubscribeRequest<OHLCParameter> buildStandardOHLCSubscribeRequest() {
        return SubscribeRequest.<OHLCParameter>builder()
            .requestId(new BigInteger("12345"))
            .params(OHLCParameter.builder()
                .interval(30)
                .symbols(List.of("BTC/USD", "BTC/EUR"))
                .build())
            .build();
    }

    public static SubscribeRequest<InstrumentParameter> buildStandardInstrumentSubscribeRequest() {
        return SubscribeRequest.<InstrumentParameter>builder()
            .requestId(new BigInteger("12345"))
            .params(InstrumentParameter.builder()
                .snapshot(true)
                .build())
            .build();
    }
}
