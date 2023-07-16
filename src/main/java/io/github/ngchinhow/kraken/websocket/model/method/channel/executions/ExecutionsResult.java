package io.github.ngchinhow.kraken.websocket.model.method.channel.executions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelResult;
import lombok.*;

@ToString
@Getter
@Setter(value = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public final class ExecutionsResult extends AbstractChannelResult {
    @JsonProperty("snapshot_trades")
    private Boolean snapshotTrades;
    @JsonProperty("order_status")
    private Boolean orderStatus;
    @JsonProperty("ratecounter")
    private Boolean rateCounter;
    @NonNull
    private String token;

    {
        this.setChannel(ChannelMetadata.ChannelType.EXECUTIONS);
    }
}