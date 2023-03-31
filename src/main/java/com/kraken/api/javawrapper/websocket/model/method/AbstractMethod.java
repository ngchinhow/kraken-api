package com.kraken.api.javawrapper.websocket.model.method;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractMethod {
    private String method;
    @JsonProperty("req_id")
    private BigInteger requestId;
}
