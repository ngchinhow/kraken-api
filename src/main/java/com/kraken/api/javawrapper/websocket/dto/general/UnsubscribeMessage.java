package com.kraken.api.javawrapper.websocket.dto.general;

import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public class UnsubscribeMessage extends BaseSubscriptionMessage {
}
