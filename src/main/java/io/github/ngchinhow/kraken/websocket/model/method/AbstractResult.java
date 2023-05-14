package io.github.ngchinhow.kraken.websocket.model.method;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.book.BookResult;
import io.github.ngchinhow.kraken.websocket.model.method.channel.executions.ExecutionsResult;
import io.github.ngchinhow.kraken.websocket.model.method.channel.instrument.InstrumentResult;
import io.github.ngchinhow.kraken.websocket.model.method.channel.ohlc.OHLCResult;
import io.github.ngchinhow.kraken.websocket.model.method.channel.ticker.TickerResult;
import io.github.ngchinhow.kraken.websocket.model.method.channel.trade.TradeResult;

/**
 * Nested class in responses from Kraken's WebSockets V2 API present across all response types, except for pong.
 *
 * @see <a href="https://docs.kraken.com/websockets-v2/#methods">Kraken Methods</a>
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = ChannelMetadata.CHANNEL,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BookResult.class, name = ChannelMetadata.ChannelType.BOOK),
    @JsonSubTypes.Type(value = ExecutionsResult.class, name = ChannelMetadata.ChannelType.EXECUTIONS),
    @JsonSubTypes.Type(value = InstrumentResult.class, name = ChannelMetadata.ChannelType.INSTRUMENT),
    @JsonSubTypes.Type(value = OHLCResult.class, name = ChannelMetadata.ChannelType.OHLC),
    @JsonSubTypes.Type(value = TickerResult.class, name = ChannelMetadata.ChannelType.TICKER),
    @JsonSubTypes.Type(value = TradeResult.class, name = ChannelMetadata.ChannelType.TRADE)
})
public interface AbstractResult {
    default String getChannel() {
        return null;
    }

    default String getSymbol() {
        return null;
    }
}
