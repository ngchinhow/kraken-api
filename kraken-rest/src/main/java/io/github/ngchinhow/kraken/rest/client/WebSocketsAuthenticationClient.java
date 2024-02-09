package io.github.ngchinhow.kraken.rest.client;

import feign.Headers;
import feign.RequestLine;
import io.github.ngchinhow.kraken.rest.model.websocketsauthentication.token.WebSocketsTokenResult;

public interface WebSocketsAuthenticationClient extends RestClient {

    @RequestLine("POST /0/private/GetWebSocketsToken")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    WebSocketsTokenResult getWebsocketsToken();
}
