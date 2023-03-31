package com.kraken.api.javawrapper.websocket.model.method.detail;

import java.util.Collections;
import java.util.List;

public interface AbstractParameter {

    default String getChannel() {
        return null;
    }

    default List<String> getSymbols() {
        return Collections.singletonList(null);
    }
}
