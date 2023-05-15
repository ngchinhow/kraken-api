package io.github.ngchinhow.kraken.rest.model.marketdata.pair;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"minVolume", "percentFee"})
public class Fee {
    private int minVolume;
    private int percentFee;
}