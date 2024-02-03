package io.github.ngchinhow.kraken.websockets.model.message;

import io.github.ngchinhow.kraken.websockets.enums.ChannelMetadata;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
public class HeartbeatMessage extends AbstractMessage {

    {
        this.setChannel(ChannelMetadata.ChannelType.HEARTBEAT);
    }
}
