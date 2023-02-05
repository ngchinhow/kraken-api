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
public class SpreadMessage extends PublicationMessage {
    private BigDecimal bid;
    private BigDecimal ask;
    private LocalDateTime time;
    private BigDecimal bidVolume;
    private BigDecimal askVolume;

    public SpreadMessage() {
        this.setChannelName(WebSocketEnumerations.CHANNEL.SPREAD);
    }
    
    public static abstract class SpreadMessageBuilder<
        C extends SpreadMessage,
        B extends SpreadMessageBuilder<C, B>
    > extends PublicationMessageBuilder<C, B> {
        public B time(String timestamp) {
            this.time = DateTimeUtils.epochMicroToLocalDateTime(timestamp);
            return this.self();
        }

        public B time(LocalDateTime time) {
            this.time = time;
            return this.self();
        }
    }
    
    public static SpreadMessage fromJsonNodeList(List<JsonNode> jsonNodeList, WebSocketEnumerations.CHANNEL channel) {
        return new SpreadMessage().toBuilder()
            .channelId(jsonNodeList.get(0).asInt())
            .channelName(channel)
            .pair(jsonNodeList.get(2).asText())
            .bid(new BigDecimal(jsonNodeList.get(1).get(0).asText()))
            .ask(new BigDecimal(jsonNodeList.get(1).get(1).asText()))
            .time(jsonNodeList.get(1).get(2).asText())
            .bidVolume(new BigDecimal(jsonNodeList.get(1).get(3).asText()))
            .askVolume(new BigDecimal(jsonNodeList.get(1).get(4).asText()))
            .build();
    }
}
