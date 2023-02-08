package com.kraken.api.javawrapper.websocket.dto.publication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.kraken.api.javawrapper.websocket.utils.DateTimeUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeEmbeddedObject {
    private BigDecimal price;
    private BigDecimal volume;
    private LocalDateTime time;
    private String side;
    private String orderType;
    private String misc;

    public static class TradeEmbeddedObjectBuilder {
        public TradeEmbeddedObjectBuilder time(String timestamp) {
            this.time = DateTimeUtils.epochMicroToLocalDateTime(timestamp);
            return this;
        }
    }
}
