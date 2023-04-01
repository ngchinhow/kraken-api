package com.kraken.api.javawrapper.websocket.model.method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;
import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractMethod {
    private String method;
    @JsonProperty("req_id")
    private BigInteger requestId;

    public RequestIdentifier toRequestIdentifier(ZonedDateTime timestamp) {
        return RequestIdentifier.builder()
            .method(method)
            .requestId(requestId)
            .timestamp(timestamp)
            .build();
    }
}
