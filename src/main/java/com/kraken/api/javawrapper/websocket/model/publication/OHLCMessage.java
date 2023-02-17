package com.kraken.api.javawrapper.websocket.model.publication;

import com.fasterxml.jackson.databind.JsonNode;
import com.kraken.api.javawrapper.websocket.dto.request.SubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import com.kraken.api.javawrapper.websocket.utils.DateTimeUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class OHLCMessage extends AbstractPublicationMessage {
    private Integer interval;
    private LocalDateTime intervalStart;
    private LocalDateTime intervalEnd;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal vwap;
    private BigDecimal volume;
    private Integer count;

    public OHLCMessage() {
        this.setChannelName(WebSocketEnumerations.CHANNEL.OHLC);
    }

    @SuppressWarnings("unused")
    public static abstract class OHLCMessageBuilder<
        C extends OHLCMessage,
        B extends OHLCMessageBuilder<C, B>
    > extends AbstractPublicationMessageBuilder<C, B> {
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

   @SuppressWarnings("unused")
    public static OHLCMessage fromJsonNodeList(List<JsonNode> jsonNodeList) {
        String[] channelInfo = jsonNodeList.get(2).asText().split("-");
        return new OHLCMessage().toBuilder()
            .channelId(jsonNodeList.get(0).asInt())
            .interval(Integer.parseInt(channelInfo[1]))
            .pair(jsonNodeList.get(3).asText())
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

    @Override
    public SubscribeRequestIdentifier toSubscribeRequestIdentifier() {
        return super.toSubscribeRequestIdentifier().toBuilder()
            .interval(this.interval)
            .build();
    }
}
