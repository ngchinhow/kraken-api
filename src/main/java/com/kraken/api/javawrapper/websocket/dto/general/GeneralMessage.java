package com.kraken.api.javawrapper.websocket.dto.general;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT.*;

@Data
@SuperBuilder
@NoArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = EVENT
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PingMessage.class, name = PING),
    @JsonSubTypes.Type(value = PongMessage.class, name = PONG),
    @JsonSubTypes.Type(value = HeartbeatMessage.class, name = HEARTBEAT),
    @JsonSubTypes.Type(value = SystemStatusMessage.class, name = SYSTEM_STATUS),
    @JsonSubTypes.Type(value = SubscribeMessage.class, name = SUBSCRIBE),
    @JsonSubTypes.Type(value = UnsubscribeMessage.class, name = UNSUBSCRIBE),
    @JsonSubTypes.Type(value = SubscriptionStatusMessage.class, name = SUBSCRIPTION_STATUS)
})
public abstract class GeneralMessage {
}
