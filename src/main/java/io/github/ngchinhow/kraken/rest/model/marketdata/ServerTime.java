package io.github.ngchinhow.kraken.rest.model.marketdata;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ngchinhow.kraken.rest.model.AbstractResult;
import lombok.Data;

import java.time.ZonedDateTime;

public abstract class ServerTime {

    @Data
    public static class Result implements AbstractResult {
        @JsonProperty(value = "unixtime", required = true)
        private int unixTime;
        @JsonProperty(value = "rfc1123", required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE',' dd MMM yy HH:mm:ss Z")
        private ZonedDateTime isoTime;
    }
}
