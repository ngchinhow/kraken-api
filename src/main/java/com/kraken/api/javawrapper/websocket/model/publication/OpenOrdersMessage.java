package com.kraken.api.javawrapper.websocket.model.publication;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class OpenOrdersMessage extends AbstractPublicationMessage {
}
