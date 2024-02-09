package io.github.ngchinhow.kraken.websockets.model.message.instrument;

import io.github.ngchinhow.kraken.websockets.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websockets.model.message.AbstractPublicationMessage;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentMessage extends AbstractPublicationMessage {
    private Data data;

    {
        this.setChannel(ChannelMetadata.ChannelType.INSTRUMENT);
    }
}
