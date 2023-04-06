package io.github.ngchinhow.kraken.rest.model.websocketsauthentication;

import io.github.ngchinhow.kraken.rest.model.AbstractResult;
import lombok.Data;

public abstract class WebSocketsToken {
    @Data
    public static class Result implements AbstractResult {
        private String token;
        private int expires;
    }
}
