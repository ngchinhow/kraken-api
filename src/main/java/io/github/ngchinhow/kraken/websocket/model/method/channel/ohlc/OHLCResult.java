package io.github.ngchinhow.kraken.websocket.model.method.channel.ohlc;

import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelResult;
import lombok.*;

@ToString
@Getter
@Setter(value = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
public final class OHLCResult extends AbstractChannelResult {
    @NonNull
    private final String symbol;
    @NonNull
    private final Integer interval;

    {
        this.setChannel(ChannelMetadata.ChannelType.OHLC);
    }
}