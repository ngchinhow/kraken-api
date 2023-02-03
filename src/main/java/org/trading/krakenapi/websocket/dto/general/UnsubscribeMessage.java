package org.trading.krakenapi.websocket.dto.general;

import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
public class UnsubscribeMessage extends BaseSubscriptionMessage {
}
