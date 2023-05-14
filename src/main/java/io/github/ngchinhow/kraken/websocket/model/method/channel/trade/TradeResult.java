package io.github.ngchinhow.kraken.websocket.model.method.channel.trade;

import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelResult;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
public class TradeResult extends AbstractChannelResult {
    @NonNull
    private String symbol;

    {
        this.setChannel(ChannelMetadata.ChannelType.TRADE);
    }
}