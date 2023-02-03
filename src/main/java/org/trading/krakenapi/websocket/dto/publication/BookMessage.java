package org.trading.krakenapi.websocket.dto.publication;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.trading.krakenapi.websocket.enums.WebSocketEnumerations;
import org.trading.krakenapi.websocket.utils.DateTimeUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class BookMessage extends PublicationMessage {
    private List<Level> askLevels;
    private List<Level> bidLevels;

    public BookMessage() {
        this.setChannelName(WebSocketEnumerations.CHANNEL.BOOK);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Level {
        private BigDecimal price;
        private BigDecimal volume;
        private LocalDateTime timestamp;

        public static class LevelBuilder {
            public LevelBuilder timestamp(String timestamp) {
                this.timestamp = DateTimeUtils.epochMicroToLocalDateTime(timestamp);
                return this;
            }
        }
    }
    
    public static BookMessage fromJsonNodeList(List<JsonNode> jsonNodeList, WebSocketEnumerations.CHANNEL channel) {
        List<Level> askLevels = new ArrayList<>();
        Iterator<JsonNode> askLevelsIterator = jsonNodeList.get(1).get("as").elements();
        while (askLevelsIterator.hasNext()) {
            JsonNode askLevelNode = askLevelsIterator.next();
            askLevels.add(Level.builder()
                .price(new BigDecimal(askLevelNode.get(0).asText()))
                .volume(new BigDecimal(askLevelNode.get(1).asText()))
                .timestamp(askLevelNode.get(2).asText())
                .build());
        }
        List<Level> bidLevels = new ArrayList<>();
        Iterator<JsonNode> bidLevelsIterator = jsonNodeList.get(1).get("bs").elements();
        while (bidLevelsIterator.hasNext()) {
            JsonNode bidLevelNode = bidLevelsIterator.next();
            bidLevels.add(Level.builder()
                .price(new BigDecimal(bidLevelNode.get(0).asText()))
                .volume(new BigDecimal(bidLevelNode.get(1).asText()))
                .timestamp(bidLevelNode.get(2).asText())
                .build());
        }
        return new BookMessage().toBuilder()
            .askLevels(askLevels)
            .bidLevels(bidLevels)
            .build();
    }
}
