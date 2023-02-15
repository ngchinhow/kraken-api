package com.kraken.api.javawrapper.websocket.dto;

import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseSubscriptionRequestIdentifier extends RequestIdentifier {
    private WebSocketEnumerations.CHANNEL channel;
    private String pair;
    private Integer depth;
    private Integer interval;
}
