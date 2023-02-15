package com.kraken.api.javawrapper.websocket.model.event;

import com.kraken.api.javawrapper.websocket.model.event.embedded.SubscriptionEmbeddedObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseSubscriptionMessage extends AbstractInteractiveMessage {
    private SubscriptionEmbeddedObject subscription;
}
