package com.kraken.api.javawrapper.websocket.model.method;

import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class AbstractRequest extends AbstractMethod {
    public RequestIdentifier toRequestIdentifier() {
        return RequestIdentifier.builder()
            .method(this.getMethod())
            .requestId(this.getRequestId())
            .build();
    }
}
