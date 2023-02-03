package org.trading.krakenapi.websocket.dto.general;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PingMessage extends GeneralMessage {
    private BigInteger reqid;
}
