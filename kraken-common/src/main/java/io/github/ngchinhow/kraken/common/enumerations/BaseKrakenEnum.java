package io.github.ngchinhow.kraken.common.enumerations;

import com.fasterxml.jackson.annotation.JsonValue;

public interface BaseKrakenEnum {

    @JsonValue
    default String toJsonString() {
        Enum<?> e = (Enum<?>) this;
        return e.name().toLowerCase().replace("_", "-");
    }
}

