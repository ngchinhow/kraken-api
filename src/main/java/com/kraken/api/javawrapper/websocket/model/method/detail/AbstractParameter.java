package com.kraken.api.javawrapper.websocket.model.method.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collections;
import java.util.List;

public interface AbstractParameter {

    default String getChannel() {
        return null;
    }

    @JsonIgnore
    default List<String> getSymbols() {
        return Collections.singletonList(null);
    }
}
