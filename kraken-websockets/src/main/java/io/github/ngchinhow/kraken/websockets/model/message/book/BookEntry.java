package io.github.ngchinhow.kraken.websockets.model.message.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor(force = true)
public class BookEntry {
    @NonNull
    private BigDecimal price;
    @JsonProperty(value = "qty", required = true)
    private BigDecimal quantity;
}
