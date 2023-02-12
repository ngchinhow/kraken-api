package com.kraken.api.javawrapper.websocket.model.publication;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class OpenOrdersMessage extends AbstractPublicationMessage {
}
