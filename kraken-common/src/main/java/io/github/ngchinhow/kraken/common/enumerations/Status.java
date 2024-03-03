package io.github.ngchinhow.kraken.common.enumerations;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    ONLINE,
    MAINTENANCE,
    CANCEL_ONLY,
    POST_ONLY;

    @JsonValue
    public String getStatus() {
        return this.name().toLowerCase();
    }
}
