package io.github.ngchinhow.kraken.websocket.model.method.channel.ohlc;

import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelResult;
import lombok.*;

@ToString
@Getter
@Setter(value = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public final class OHLCResult extends AbstractChannelResult {
    @NonNull
    private String symbol;
    @NonNull
    private Integer interval;

    {
        this.setChannel(ChannelMetadata.ChannelType.OHLC);
    }
}