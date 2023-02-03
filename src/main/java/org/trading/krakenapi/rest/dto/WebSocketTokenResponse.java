package org.trading.krakenapi.rest.dto;

import lombok.Data;

@Data
public class WebSocketTokenResponse {
    private String token;
    private int expires;
}
