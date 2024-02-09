package io.github.ngchinhow.kraken.websockets.model.method.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.ngchinhow.kraken.websockets.model.method.ParameterInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractChannelParameter implements ParameterInterface {
    private String channel;
    private Boolean snapshot;

    @JsonIgnore
    public List<String> getSymbols() {
        return Collections.singletonList(null);
    }
}
