package io.github.ngchinhow.kraken.websocket.model.message.executions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Fee {
    private String asset;
    @JsonProperty("qty")
    private BigDecimal quantity;
}
