package io.github.ngchinhow.kraken.websocket.model.method.channel.ohlc;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelParameter;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
public class OHLCParameter extends AbstractChannelParameter {
    @JsonProperty(value = "symbol", required = true)
    private List<String> symbols;
    @NonNull
    private Integer interval;

    {
        this.setChannel(ChannelMetadata.ChannelType.OHLC);
    }
}