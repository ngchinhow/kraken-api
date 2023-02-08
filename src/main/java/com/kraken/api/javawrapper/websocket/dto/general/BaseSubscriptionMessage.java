package com.kraken.api.javawrapper.websocket.dto.general;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseSubscriptionMessage extends GeneralMessage {
    private String reqid;
    private List<String> pair;
    private SubscriptionEmbeddedObject subscription;
}
