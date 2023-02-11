package com.kraken.api.javawrapper.websocket.dto.publication;

import com.fasterxml.jackson.databind.JsonNode;
import com.kraken.api.javawrapper.websocket.utils.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookLevelEmbeddedObject {
    private BigDecimal price;
    private BigDecimal volume;
    private LocalDateTime timestamp;
    private String updateType;

    @SuppressWarnings("unused")
    public static class BookLevelEmbeddedObjectBuilder {
        public BookLevelEmbeddedObjectBuilder timestamp(String timestamp) {
            this.timestamp = DateTimeUtils.epochMicroToLocalDateTime(timestamp);
            return this;
        }

        public BookLevelEmbeddedObjectBuilder updateType(JsonNode node) {
            if (Objects.nonNull(node))
                this.updateType = node.asText();
            return this;
        }
    }
}
