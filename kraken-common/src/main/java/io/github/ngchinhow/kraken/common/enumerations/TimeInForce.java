package io.github.ngchinhow.kraken.common.enumerations;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TimeInForce {
    IOC,
    GTD,
    GTC;

    @JsonValue
    public String toJsonString() {
        return this.name();
    }
}
