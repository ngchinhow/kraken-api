package io.github.ngchinhow.kraken.rest.model.marketdata.spot.servertime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.rest.model.ResultInterface;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public final class ServerTimeResult implements ResultInterface {
    @JsonProperty(value = "unixtime", required = true)
    private int unixTime;
    @JsonProperty(value = "rfc1123", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE',' dd MMM yy HH:mm:ss Z")
    private ZonedDateTime isoTime;
}