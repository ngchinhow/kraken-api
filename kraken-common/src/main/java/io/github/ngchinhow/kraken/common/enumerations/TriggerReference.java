package io.github.ngchinhow.kraken.common.enumerations;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TriggerReference {
    INDEX,
    LAST;

    @JsonValue
    public String toJsonString() {
        return this.name().toLowerCase();
    }
}
