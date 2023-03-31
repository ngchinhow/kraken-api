package com.kraken.api.javawrapper.websocket.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kraken.api.javawrapper.websocket.enums.ChannelMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigInteger;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class StatusMessage extends AbstractMessage {
    private List<SystemStatus> data;

    {
        this.setChannel(ChannelMetadata.ChannelType.STATUS);
    }

    @Data
    public static class SystemStatus {
        @JsonProperty("connection_id")
        private BigInteger connectionId;
        private System system;
        @JsonProperty("api_version")
        private String apiVersion;
        private String version;
    }

    @SuppressWarnings("unused")
    public enum System {
        ONLINE("online"),
        MAINTENANCE("maintenance"),
        CANCEL_ONLY("cancel_only"),
        POST_ONLY("post_only");

        private final String system;

        System(String system) {
            this.system = system;
        }

        @JsonValue
        public String getSystem() {
            return this.system;
        }
    }
}
