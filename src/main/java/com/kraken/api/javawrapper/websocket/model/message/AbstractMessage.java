package com.kraken.api.javawrapper.websocket.model.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kraken.api.javawrapper.websocket.enums.ChannelMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.kraken.api.javawrapper.websocket.enums.ChannelMetadata.ChannelType.*;
import static com.kraken.api.javawrapper.websocket.enums.ChannelMetadata.CHANNEL;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = CHANNEL,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BookMessage.class, name = BOOK),
    @JsonSubTypes.Type(value = ExecutionsMessage.class, name = EXECUTIONS),
    @JsonSubTypes.Type(value = HeartbeatMessage.class, name = HEARTBEAT),
    @JsonSubTypes.Type(value = InstrumentMessage.class, name = INSTRUMENT),
    @JsonSubTypes.Type(value = OHLCMessage.class, name = OHLC),
    @JsonSubTypes.Type(value = TickerMessage.class, name = TICKER),
    @JsonSubTypes.Type(value = TradeMessage.class, name = TRADE),
    @JsonSubTypes.Type(value = StatusMessage.class, name = STATUS)
})
public abstract class AbstractMessage {
    private String channel;
    private ChannelMetadata.ChangeType type;
}
