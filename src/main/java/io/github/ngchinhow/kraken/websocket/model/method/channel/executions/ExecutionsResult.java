package io.github.ngchinhow.kraken.websocket.model.method.channel.executions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelResult;
import lombok.*;

@ToString
@Getter
@Setter(value = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
public final class ExecutionsResult extends AbstractChannelResult {
    @JsonProperty("snapshot_trades")
    private final Boolean snapshotTrades;
    @JsonProperty("order_status")
    private final Boolean orderStatus;
    @JsonProperty("ratecounter")
    private final Boolean rateCounter;
    @NonNull
    private final String token;

    {
        this.setChannel(ChannelMetadata.ChannelType.EXECUTIONS);
    }
}