package io.github.ngchinhow.kraken.websockets.model.method.channel.ticker;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websockets.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelParameter;
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
public final class TickerParameter extends AbstractChannelParameter {
    @JsonProperty(value = "symbol", required = true)
    private List<String> symbols;

    {
        setChannel(ChannelMetadata.ChannelType.TICKER);
    }
}