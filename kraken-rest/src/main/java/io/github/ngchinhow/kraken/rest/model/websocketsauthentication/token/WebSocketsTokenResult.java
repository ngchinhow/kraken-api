package io.github.ngchinhow.kraken.rest.model.websocketsauthentication.token;

import io.github.ngchinhow.kraken.rest.model.ResultInterface;
import lombok.Data;

@Data
public class WebSocketsTokenResult implements ResultInterface {
    private String token;
    private int expires;
}