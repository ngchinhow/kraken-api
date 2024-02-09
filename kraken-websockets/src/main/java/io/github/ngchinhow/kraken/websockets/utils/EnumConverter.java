package io.github.ngchinhow.kraken.websockets.utils;

import com.fasterxml.jackson.annotation.JsonValue;

public interface EnumConverter {

    @JsonValue
    default String toJsonString() {
        Enum<?> e = (Enum<?>) this;
        return e.name().toLowerCase().replace("_", "-");
    }
}
