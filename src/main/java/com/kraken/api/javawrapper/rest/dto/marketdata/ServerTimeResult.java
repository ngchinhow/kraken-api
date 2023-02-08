package com.kraken.api.javawrapper.rest.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ServerTimeResult {
    @JsonProperty("unixtime")
    private int unixTime;

    private String rfc1123;
}
