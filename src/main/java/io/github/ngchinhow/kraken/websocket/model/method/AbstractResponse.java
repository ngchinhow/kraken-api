package io.github.ngchinhow.kraken.websocket.model.method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.ngchinhow.kraken.websocket.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websocket.enums.MethodMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = MethodMetadata.METHOD,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Echo.PongResponse.class, name = MethodMetadata.MethodType.PONG),
    @JsonSubTypes.Type(value = Subscription.SubscribeResponse.class, name = MethodMetadata.MethodType.SUBSCRIBE),
    @JsonSubTypes.Type(value = Unsubscription.UnsubscribeResponse.class, name = MethodMetadata.MethodType.UNSUBSCRIBE)
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
