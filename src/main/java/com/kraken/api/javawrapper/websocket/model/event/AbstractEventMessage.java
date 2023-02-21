package com.kraken.api.javawrapper.websocket.model.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kraken.api.javawrapper.websocket.model.event.request.PingMessage;
import com.kraken.api.javawrapper.websocket.model.event.request.SubscribeMessage;
import com.kraken.api.javawrapper.websocket.model.event.request.UnsubscribeMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.PongMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.SubscriptionStatusMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT;
import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.EVENT_TYPE.*;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = EVENT,
    visible = true
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
public abstract class AbstractEventMessage {
    private String event;
}
