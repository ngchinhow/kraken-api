package io.github.ngchinhow.kraken.websocket.model.message.ticker;

import io.github.ngchinhow.kraken.websocket.dto.request.SubscriptionRequestIdentifier;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractPublicationMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class TickerMessage extends AbstractPublicationMessage {
    private List<Ticker> data;

    {
        this.setChannel(ChannelMetadata.ChannelType.TICKER);
    }

    @Override
    public SubscriptionRequestIdentifier toRequestIdentifier() {
        return super.toRequestIdentifier().toBuilder()
            .symbol(data.get(0).getSymbol())
            .build();
    }
}
