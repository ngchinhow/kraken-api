package io.github.ngchinhow.kraken.websocket.model.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.message.book.BookMessage;
import io.github.ngchinhow.kraken.websocket.model.message.executions.ExecutionsMessage;
import io.github.ngchinhow.kraken.websocket.model.message.instrument.InstrumentMessage;
import io.github.ngchinhow.kraken.websocket.model.message.ohlc.OHLCMessage;
import io.github.ngchinhow.kraken.websocket.model.message.status.StatusMessage;
import io.github.ngchinhow.kraken.websocket.model.message.ticker.TickerMessage;
import io.github.ngchinhow.kraken.websocket.model.message.trade.TradeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = ChannelMetadata.CHANNEL,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BookMessage.class, name = ChannelMetadata.ChannelType.BOOK),
    @JsonSubTypes.Type(value = ExecutionsMessage.class, name = ChannelMetadata.ChannelType.EXECUTIONS),
    @JsonSubTypes.Type(value = HeartbeatMessage.class, name = ChannelMetadata.ChannelType.HEARTBEAT),
    @JsonSubTypes.Type(value = InstrumentMessage.class, name = ChannelMetadata.ChannelType.INSTRUMENT),
    @JsonSubTypes.Type(value = OHLCMessage.class, name = ChannelMetadata.ChannelType.OHLC),
    @JsonSubTypes.Type(value = TickerMessage.class, name = ChannelMetadata.ChannelType.TICKER),
    @JsonSubTypes.Type(value = TradeMessage.class, name = ChannelMetadata.ChannelType.TRADE),
    @JsonSubTypes.Type(value = StatusMessage.class, name = ChannelMetadata.ChannelType.STATUS)
})
public abstract class AbstractMessage {
    private String channel;
    private ChannelMetadata.ChangeType type;
}
