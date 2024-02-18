package io.github.ngchinhow.kraken.websockets.model.method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.ngchinhow.kraken.websockets.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websockets.enums.MethodMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.echo.PongResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.add.AddOrderResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.batchadd.BatchAddOrdersResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.edit.EditOrderResponse;
import io.github.ngchinhow.kraken.websockets.model.method.subscription.SubscribeResponse;
import io.github.ngchinhow.kraken.websockets.model.method.unsubscription.UnsubscribeResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@ToString
@Getter
@Setter(value = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor(force = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = MethodMetadata.METHOD,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PongResponse.class, name = MethodMetadata.MethodType.PONG),
    @JsonSubTypes.Type(value = SubscribeResponse.class, name = MethodMetadata.MethodType.SUBSCRIBE),
    @JsonSubTypes.Type(value = UnsubscribeResponse.class, name = MethodMetadata.MethodType.UNSUBSCRIBE),
    @JsonSubTypes.Type(value = AddOrderResponse.class, name = MethodMetadata.MethodType.ADD_ORDER),
    @JsonSubTypes.Type(value = BatchAddOrdersResponse.class, name = MethodMetadata.MethodType.BATCH_ADD),
    @JsonSubTypes.Type(value = EditOrderResponse.class, name = MethodMetadata.MethodType.EDIT_ORDER)
})
public abstract class AbstractResponse extends AbstractMethod {
    @JsonProperty("time_in")
    private final ZonedDateTime timeIn;
    @JsonProperty("time_out")
    private final ZonedDateTime timeOut;

    public RequestIdentifier toRequestIdentifier() {
        return super.toRequestIdentifier(timeIn);
    }
}
