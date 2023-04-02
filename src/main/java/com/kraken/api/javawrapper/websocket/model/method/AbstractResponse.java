package com.kraken.api.javawrapper.websocket.model.method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

import static com.kraken.api.javawrapper.websocket.enums.MethodMetadata.METHOD;
import static com.kraken.api.javawrapper.websocket.enums.MethodMetadata.MethodType.*;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = METHOD,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Echo.PongResponse.class, name = PONG),
    @JsonSubTypes.Type(value = Subscription.SubscribeResponse.class, name = SUBSCRIBE),
    @JsonSubTypes.Type(value = Unsubscription.UnsubscribeResponse.class, name = UNSUBSCRIBE)
})
public abstract class AbstractResponse extends AbstractMethod {
    @JsonProperty("time_in")
    private ZonedDateTime timeIn;
    @JsonProperty("time_out")
    private ZonedDateTime timeOut;

    public RequestIdentifier toRequestIdentifier() {
        return super.toRequestIdentifier(timeIn);
    }
}
