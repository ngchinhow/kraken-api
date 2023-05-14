package io.github.ngchinhow.kraken.websocket.model.message.ohlc;

import io.github.ngchinhow.kraken.websocket.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractPublicationMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class OHLCMessage extends AbstractPublicationMessage {
    private List<OHLC> data;
    private ZonedDateTime timestamp;

    {
        this.setChannel(ChannelMetadata.ChannelType.OHLC);
    }

    @Override
    public RequestIdentifier toRequestIdentifier() {
        return super.toRequestIdentifier().toBuilder()
            .symbol(data.get(0).getSymbol())
            .build();
    }
}
