package com.kraken.api.javawrapper.websocket.model.publication;

import com.fasterxml.jackson.databind.JsonNode;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import com.kraken.api.javawrapper.websocket.utils.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class SpreadMessage extends AbstractPublicationMessage {
    private BigDecimal bid;
    private BigDecimal ask;
    private LocalDateTime time;
    private BigDecimal bidVolume;
    private BigDecimal askVolume;

    public SpreadMessage() {
        this.setChannelName(WebSocketEnumerations.CHANNEL.SPREAD);
    }

    @SuppressWarnings("unused")
    public static abstract class SpreadMessageBuilder<
        C extends SpreadMessage,
        B extends SpreadMessageBuilder<C, B>
    > extends AbstractPublicationMessageBuilder<C, B> {
        public B time(String timestamp) {
            this.time = DateTimeUtils.epochMicroToLocalDateTime(timestamp);
            return this.self();
        }

        public B time(LocalDateTime time) {
            this.time = time;
            return this.self();
        }
    }

    @SuppressWarnings("unused")
    public static SpreadMessage fromJsonNodeList(List<JsonNode> jsonNodeList) {
        return new SpreadMessage().toBuilder()
            .channelId(jsonNodeList.get(0).asInt())
            .pair(jsonNodeList.get(2).asText())
            .bid(new BigDecimal(jsonNodeList.get(1).get(0).asText()))
            .ask(new BigDecimal(jsonNodeList.get(1).get(1).asText()))
            .time(jsonNodeList.get(1).get(2).asText())
            .bidVolume(new BigDecimal(jsonNodeList.get(1).get(3).asText()))
            .askVolume(new BigDecimal(jsonNodeList.get(1).get(4).asText()))
            .build();
    }
}
