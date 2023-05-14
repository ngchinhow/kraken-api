package io.github.ngchinhow.kraken.websocket.model.message.executions;

import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractPublicationMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
public class ExecutionsMessage extends AbstractPublicationMessage {
    private final List<Execution> data;
    private final int sequence;

    {
        this.setChannel(ChannelMetadata.ChannelType.EXECUTIONS);
    }
}
