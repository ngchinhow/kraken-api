package io.github.ngchinhow.kraken.websockets.model.method.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigInteger;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public final class BaseOrderOutput extends BaseOrderId {
    @JsonProperty("order_userref")
    private BigInteger orderUserReference;
    private String validation;
}
