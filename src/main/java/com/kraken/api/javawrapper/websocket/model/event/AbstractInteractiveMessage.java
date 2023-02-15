package com.kraken.api.javawrapper.websocket.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractInteractiveMessage extends AbstractEventMessage {
    @JsonProperty("reqid")
    private BigInteger reqId;
}
