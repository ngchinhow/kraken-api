package io.github.ngchinhow.kraken.rest.model.websocketsauthentication.token;

import io.github.ngchinhow.kraken.rest.model.AbstractResult;
import lombok.Data;

@Data
public class WebSocketsTokenResult implements AbstractResult {
    private String token;
    private int expires;
}