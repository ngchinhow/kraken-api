package io.github.ngchinhow.kraken.websocket.model.message.status;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class SystemStatus {
    @JsonProperty("connection_id")
    private BigInteger connectionId;
    private System system;
    @JsonProperty("api_version")
    private String apiVersion;
    private String version;

    @Getter(onMethod = @__(@JsonValue))
    @RequiredArgsConstructor
    public enum System {
        ONLINE("online"),
        MAINTENANCE("maintenance"),
        CANCEL_ONLY("cancel_only"),
        POST_ONLY("post_only");

        private final String system;
    }
}
