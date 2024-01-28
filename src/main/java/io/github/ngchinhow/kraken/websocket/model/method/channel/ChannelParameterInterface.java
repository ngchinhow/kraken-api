package io.github.ngchinhow.kraken.websocket.model.method.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.ngchinhow.kraken.websocket.model.method.ParameterInterface;

import java.util.Collections;
import java.util.List;

public interface ChannelParameterInterface extends ParameterInterface {

    String getChannel();

    void setChannel(String channel);

    Boolean getSnapshot();

    void setSnapshot(Boolean snapshot);

    @JsonIgnore
    default List<String> getSymbols() {
        return Collections.singletonList(null);
    }
}