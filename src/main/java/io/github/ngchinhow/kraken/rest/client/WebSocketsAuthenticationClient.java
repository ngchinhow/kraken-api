package io.github.ngchinhow.kraken.rest.client;

import io.github.ngchinhow.kraken.rest.dto.KrakenResponse;
import io.github.ngchinhow.kraken.rest.dto.websocketsauthentication.WebSocketsTokenResult;
import feign.Headers;
import feign.RequestLine;

public interface WebSocketsAuthenticationClient extends RestClient {

    @RequestLine("POST /private/GetWebSocketsToken")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    KrakenResponse<WebSocketsTokenResult> getWebsocketsToken();
}
