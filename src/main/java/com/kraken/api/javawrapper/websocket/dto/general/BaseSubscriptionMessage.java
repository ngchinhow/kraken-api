package com.kraken.api.javawrapper.websocket.dto.general;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigInteger;

@Getter
@Setter
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class BaseSubscriptionMessage extends GeneralMessage {
    private BigInteger reqid;
    private SubscriptionEmbeddedObject subscription;
}
