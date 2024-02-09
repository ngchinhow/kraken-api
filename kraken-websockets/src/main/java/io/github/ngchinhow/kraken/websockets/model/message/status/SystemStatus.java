package io.github.ngchinhow.kraken.websockets.model.message.status;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.common.enumerations.BaseKrakenEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public enum System implements BaseKrakenEnum {
        ONLINE,
        MAINTENANCE,
        CANCEL_ONLY,
        POST_ONLY
    }
}
