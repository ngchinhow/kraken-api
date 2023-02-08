package com.kraken.api.javawrapper.rest.dto.websocketsauthentication;

import lombok.Data;

@Data
public class WebSocketsTokenResult {
    private String token;
    private int expires;
}
