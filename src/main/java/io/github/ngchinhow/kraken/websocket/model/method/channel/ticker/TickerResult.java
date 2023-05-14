package io.github.ngchinhow.kraken.websocket.model.method.channel.ticker;

import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
public class TickerResult extends AbstractChannelResult {
    @NonNull
    private String symbol;

    {
        this.setChannel(ChannelMetadata.ChannelType.TICKER);
    }
}