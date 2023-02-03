package org.trading.krakenapi.websocket.dto.general;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static org.trading.krakenapi.websocket.enums.WebSocketEnumerations.*;

@Data
@SuperBuilder
@NoArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "event"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PingMessage.class, name = PING_EVENT),
    @JsonSubTypes.Type(value = PongMessage.class, name = PONG_EVENT),
    @JsonSubTypes.Type(value = HeartbeatMessage.class, name = HEARTBEAT_EVENT),
    @JsonSubTypes.Type(value = SystemStatusMessage.class, name = SYSTEM_STATUS_EVENT),
    @JsonSubTypes.Type(value = SubscribeMessage.class, name = SUBSCRIBE_EVENT),
    @JsonSubTypes.Type(value = UnsubscribeMessage.class, name = UNSUBSCRIBE_EVENT),
    @JsonSubTypes.Type(value = SubscriptionStatusMessage.class, name = SUBSCRIPTION_STATUS_EVENT)
})
public abstract class GeneralMessage {
}
