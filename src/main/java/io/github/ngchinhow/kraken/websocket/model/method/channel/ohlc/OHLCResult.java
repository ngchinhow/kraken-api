package io.github.ngchinhow.kraken.websocket.model.method.channel.ohlc;

import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
public class OHLCResult extends AbstractChannelResult {
    @NonNull
    private String symbol;
    @NonNull
    private Integer interval;

    {
        this.setChannel(ChannelMetadata.ChannelType.OHLC);
    }
}