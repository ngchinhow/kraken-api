package io.github.ngchinhow.kraken.rest.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ServerTimeResult {
    @JsonProperty(value = "unixtime", required = true)
    private int unixTime;
    @JsonProperty(value = "rfc1123",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE',' dd MMM yy HH:mm:ss Z")
    private ZonedDateTime isoTime;
}
