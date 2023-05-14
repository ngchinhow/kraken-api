package io.github.ngchinhow.kraken.websocket.model.method.channel.ticker;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelParameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TickerParameter extends AbstractChannelParameter {
    @JsonProperty(value = "symbol", required = true)
    private List<String> symbols;

    {
        this.setChannel(ChannelMetadata.ChannelType.TICKER);
    }
}