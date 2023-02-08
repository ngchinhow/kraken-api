package com.kraken.api.javawrapper.rest.client;

import com.kraken.api.javawrapper.rest.dto.KrakenResponse;
import com.kraken.api.javawrapper.rest.dto.websocketsauthentication.WebSocketsTokenResult;
import feign.Headers;
import feign.RequestLine;

public interface WebSocketsAuthenticationClient extends RestClient {

    @RequestLine("POST /private/GetWebSocketsToken")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    KrakenResponse<WebSocketsTokenResult> getWebsocketsToken();
}
