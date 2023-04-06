package io.github.ngchinhow.kraken.websocket.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.websocket.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class TradeMessage extends AbstractPublicationMessage {
    private List<Trade> data;

    {
        this.setChannel(ChannelMetadata.ChannelType.TRADE);
    }

    @Override
    public RequestIdentifier toRequestIdentifier() {
        return super.toRequestIdentifier().toBuilder()
            .symbol(data.get(0).symbol)
            .build();
    }

    @Data
    @NoArgsConstructor(force = true)
    public static class Trade {
        @JsonProperty(value = "ord_type", required = true)
        private String orderType;
        @NonNull
        private BigDecimal price;
        @JsonProperty(value = "qty", required = true)
        private BigDecimal quantity;
        @NonNull
        private String side;
        @NonNull
        private String symbol;
        @NonNull
        private ZonedDateTime timestamp;
        @JsonProperty(value = "trade_id", required = true)
        private Integer tradeId;
    }
}
