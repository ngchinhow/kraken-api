package org.trading.krakenapi.rest.client;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.trading.krakenapi.rest.dto.KrakenResponse;
import org.trading.krakenapi.rest.dto.WebSocketTokenResponse;

import java.util.Map;

public interface WebSocketsAuthenticationClient extends RestClient {

    @PostMapping(value = "/0/private/GetWebSocketsToken", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KrakenResponse<WebSocketTokenResponse> getWebsocketsToken(@RequestBody Map<String, ?> form);
}
