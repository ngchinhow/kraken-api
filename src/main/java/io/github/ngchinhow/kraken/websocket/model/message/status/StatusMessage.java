package io.github.ngchinhow.kraken.websocket.model.message.status;

import com.fasterxml.jackson.annotation.JsonValue;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractMessage;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class StatusMessage extends AbstractMessage {
    private List<SystemStatus> data;

    {
        this.setChannel(ChannelMetadata.ChannelType.STATUS);
    }

    @Getter(onMethod = @__({@JsonValue}))
    @RequiredArgsConstructor
    public enum System {
        ONLINE("online"),
        MAINTENANCE("maintenance"),
        CANCEL_ONLY("cancel_only"),
        POST_ONLY("post_only");

        private final String system;
    }
}
