package io.github.ngchinhow.kraken.websocket.model.method.channel.executions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.PrivateParameterInterface;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelParameter;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public final class ExecutionsParameter extends AbstractChannelParameter implements PrivateParameterInterface {
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