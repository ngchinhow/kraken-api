package io.github.ngchinhow.kraken.rest.model.marketdata.pair;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"minVolume", "percentFee"})
public class Fee {
    private BigInteger minVolume;
    private BigDecimal percentFee;
}