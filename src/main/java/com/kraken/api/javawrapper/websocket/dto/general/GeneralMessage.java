package com.kraken.api.javawrapper.websocket.dto.general;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "event"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PingMessage.class, name = WebSocketEnumerations.PING_EVENT),
    @JsonSubTypes.Type(value = PongMessage.class, name = WebSocketEnumerations.PONG_EVENT),
    @JsonSubTypes.Type(value = HeartbeatMessage.class, name = WebSocketEnumerations.HEARTBEAT_EVENT),
    @JsonSubTypes.Type(value = SystemStatusMessage.class, name = WebSocketEnumerations.SYSTEM_STATUS_EVENT),
    @JsonSubTypes.Type(value = SubscribeMessage.class, name = WebSocketEnumerations.SUBSCRIBE_EVENT),
    @JsonSubTypes.Type(value = UnsubscribeMessage.class, name = WebSocketEnumerations.UNSUBSCRIBE_EVENT),
    @JsonSubTypes.Type(value = SubscriptionStatusMessage.class, name = WebSocketEnumerations.SUBSCRIPTION_STATUS_EVENT)
})
public abstract class GeneralMessage {
}
