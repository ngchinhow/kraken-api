package org.trading.krakenapi.websocket.dto.publication;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.trading.krakenapi.websocket.enums.WebSocketEnumerations;
import org.trading.krakenapi.websocket.utils.DateTimeUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class OHLCMessage extends PublicationMessage {
    private LocalDateTime intervalStart;
    private LocalDateTime intervalEnd;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal vwap;
    private BigDecimal volume;
    private int count;

    public OHLCMessage() {
        this.setChannelName(WebSocketEnumerations.CHANNEL.OHLC);
    }

    public static abstract class OHLCMessageBuilder<
        C extends OHLCMessage,
        B extends OHLCMessageBuilder<C, B>
    > extends PublicationMessageBuilder<C, B> {
        public B intervalStart(String timestamp) {
            this.intervalStart = DateTimeUtils.epochMicroToLocalDateTime(timestamp);
            return this.self();
        }

        public B intervalStart(LocalDateTime intervalStart) {
            this.intervalStart = intervalStart;
            return this.self();
        }

        public B intervalEnd(String timestamp) {
            this.intervalEnd = DateTimeUtils.epochMicroToLocalDateTime(timestamp);
            return this.self();
        }

        public B intervalEnd(LocalDateTime intervalEnd) {
            this.intervalEnd = intervalEnd;
            return this.self();
        }
    }

    public static OHLCMessage fromJsonNodeList(List<JsonNode> jsonNodeList, WebSocketEnumerations.CHANNEL channel) {
        return new OHLCMessage().toBuilder()
            .channelId(jsonNodeList.get(0).asInt())
            .channelName(channel)
            .pair(jsonNodeList.get(2).asText())
            .intervalStart(jsonNodeList.get(1).get(0).asText())
            .intervalEnd(jsonNodeList.get(1).get(1).asText())
            .open(new BigDecimal(jsonNodeList.get(1).get(2).asText()))
            .high(new BigDecimal(jsonNodeList.get(1).get(3).asText()))
            .low(new BigDecimal(jsonNodeList.get(1).get(4).asText()))
            .close(new BigDecimal(jsonNodeList.get(1).get(5).asText()))
            .vwap(new BigDecimal(jsonNodeList.get(1).get(6).asText()))
            .volume(new BigDecimal(jsonNodeList.get(1).get(7).asText()))
            .count(jsonNodeList.get(1).get(8).asInt())
            .build();
    }
}
