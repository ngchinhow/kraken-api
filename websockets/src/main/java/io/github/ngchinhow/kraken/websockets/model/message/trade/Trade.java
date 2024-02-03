package io.github.ngchinhow.kraken.websockets.model.message.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor(force = true)
public class Trade {
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