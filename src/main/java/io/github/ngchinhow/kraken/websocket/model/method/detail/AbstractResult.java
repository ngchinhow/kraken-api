package io.github.ngchinhow.kraken.websocket.model.method.detail;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.detail.channel.*;

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
    @JsonSubTypes.Type(value = BookSubscription.Result.class, name = ChannelMetadata.ChannelType.BOOK),
    @JsonSubTypes.Type(value = ExecutionsSubscription.Result.class, name = ChannelMetadata.ChannelType.EXECUTIONS),
    @JsonSubTypes.Type(value = InstrumentSubscription.Result.class, name = ChannelMetadata.ChannelType.INSTRUMENT),
    @JsonSubTypes.Type(value = OHLCSubscription.Result.class, name = ChannelMetadata.ChannelType.OHLC),
    @JsonSubTypes.Type(value = TickerSubscription.Result.class, name = ChannelMetadata.ChannelType.TICKER),
    @JsonSubTypes.Type(value = TradeSubscription.Result.class, name = ChannelMetadata.ChannelType.TRADE)
})
public interface AbstractResult {
    default String getChannel() {
        return null;
    }

    default String getSymbol() {
        return null;
    }
}
