package com.kraken.api.javawrapper.websocket.model.method.detail;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kraken.api.javawrapper.websocket.model.method.detail.channel.*;

import static com.kraken.api.javawrapper.websocket.enums.ChannelMetadata.CHANNEL;
import static com.kraken.api.javawrapper.websocket.enums.ChannelMetadata.ChannelType.*;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = CHANNEL,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BookSubscription.Result.class, name = BOOK),
    @JsonSubTypes.Type(value = ExecutionsSubscription.Result.class, name = EXECUTIONS),
    @JsonSubTypes.Type(value = InstrumentSubscription.Result.class, name = INSTRUMENT),
    @JsonSubTypes.Type(value = OHLCSubscription.Result.class, name = OHLC),
    @JsonSubTypes.Type(value = TickerSubscription.Result.class, name = TICKER),
    @JsonSubTypes.Type(value = TradeSubscription.Result.class, name = TRADE)
})
public interface AbstractResult {
    default String getChannel() {
        return null;
    }

    default String getSymbol() {
        return null;
    }
}
