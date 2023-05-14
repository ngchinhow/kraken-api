package io.github.ngchinhow.kraken.websocket.model.message.status;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class SystemStatus {
    @JsonProperty("connection_id")
    private BigInteger connectionId;
    private StatusMessage.System system;
    @JsonProperty("api_version")
    private String apiVersion;
    private String version;
}
