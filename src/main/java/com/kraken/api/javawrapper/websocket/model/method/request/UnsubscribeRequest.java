package com.kraken.api.javawrapper.websocket.model.method.request;

import com.kraken.api.javawrapper.websocket.enums.MethodMetadata;
import com.kraken.api.javawrapper.websocket.model.method.Interaction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
public class UnsubscribeRequest extends Interaction.AbstractInteractionRequest {

    {
        this.setMethod(MethodMetadata.MethodType.UNSUBSCRIBE);
    }

//    public List<UnsubscribeRequestIdentifier> toRequestIdentifiers() {
//        return this.pairs.stream()
//            .map(p -> UnsubscribeRequestIdentifier.builder()
//                .reqId(this.getReqId())
//                .channel(this.getSubscription().getName())
//                .pair(p)
//                .depth(this.getSubscription().getDepth())
//                .interval(this.getSubscription().getInterval())
//                .build())
//            .collect(Collectors.toUnmodifiableList());
//    }
}
