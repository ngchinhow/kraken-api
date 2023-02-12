package com.kraken.api.javawrapper.websocket.model.event.request;

import com.kraken.api.javawrapper.websocket.dto.BaseSubscriptionRequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.event.BaseSubscriptionMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class UnsubscribeMessage extends BaseSubscriptionMessage {
    private List<String> pair;

    public List<BaseSubscriptionRequestIdentifier> toRequestIdentifier() {
        return this.pair.stream().map(p -> new BaseSubscriptionRequestIdentifier()).toList();
    }
}
