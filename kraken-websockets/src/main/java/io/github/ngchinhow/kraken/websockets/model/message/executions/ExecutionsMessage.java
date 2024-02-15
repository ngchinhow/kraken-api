package io.github.ngchinhow.kraken.websockets.model.message.executions;

import io.github.ngchinhow.kraken.websockets.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websockets.model.message.AbstractPublicationMessage;
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
        setChannel(ChannelMetadata.ChannelType.EXECUTIONS);
    }
}
