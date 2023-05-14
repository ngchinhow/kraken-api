package io.github.ngchinhow.kraken.websocket.model.method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.ngchinhow.kraken.websocket.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websocket.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.echo.PongResponse;
import io.github.ngchinhow.kraken.websocket.model.method.subscription.SubscribeResponse;
import io.github.ngchinhow.kraken.websocket.model.method.unsubscription.UnsubscribeResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = MethodMetadata.METHOD,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PongResponse.class, name = MethodMetadata.MethodType.PONG),
    @JsonSubTypes.Type(value = SubscribeResponse.class, name = MethodMetadata.MethodType.SUBSCRIBE),
    @JsonSubTypes.Type(value = UnsubscribeResponse.class, name = MethodMetadata.MethodType.UNSUBSCRIBE)
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
