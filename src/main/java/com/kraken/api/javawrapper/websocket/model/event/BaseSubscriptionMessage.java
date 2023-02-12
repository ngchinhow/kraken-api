package com.kraken.api.javawrapper.websocket.model.event;

import com.kraken.api.javawrapper.websocket.model.event.embedded.SubscriptionEmbeddedObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseSubscriptionMessage extends AbstractInteractiveMessage {
    private SubscriptionEmbeddedObject subscription;
}
