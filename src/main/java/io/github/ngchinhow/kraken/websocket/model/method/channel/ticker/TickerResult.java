package io.github.ngchinhow.kraken.websocket.model.method.channel.ticker;

import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelResult;
import lombok.*;

@ToString
@Getter
@Setter(value = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
public final class TickerResult extends AbstractChannelResult {
    @NonNull
    private final String symbol;

    {
        this.setChannel(ChannelMetadata.ChannelType.TICKER);
    }
}