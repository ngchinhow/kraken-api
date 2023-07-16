package io.github.ngchinhow.kraken.websocket.model.method.channel.ticker;

import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelResult;
import lombok.*;

@ToString
@Getter
@Setter(value = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public final class TickerResult extends AbstractChannelResult {
    @NonNull
    private String symbol;

    {
        this.setChannel(ChannelMetadata.ChannelType.TICKER);
    }
}