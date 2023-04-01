package com.kraken.api.javawrapper.websocket.model.method;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class AbstractRequest extends AbstractMethod {
}
