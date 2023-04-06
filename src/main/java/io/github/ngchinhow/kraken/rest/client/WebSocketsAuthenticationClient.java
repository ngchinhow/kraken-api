package io.github.ngchinhow.kraken.rest.client;

import io.github.ngchinhow.kraken.rest.model.KrakenResponse;
import io.github.ngchinhow.kraken.rest.model.websocketsauthentication.WebSocketsToken;
import feign.Headers;
import feign.RequestLine;

public interface WebSocketsAuthenticationClient extends RestClient {

    @RequestLine("POST /0/private/GetWebSocketsToken")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    KrakenResponse<WebSocketsToken.Result> getWebsocketsToken();
}
