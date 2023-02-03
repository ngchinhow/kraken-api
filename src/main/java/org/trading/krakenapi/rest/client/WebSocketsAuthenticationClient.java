package org.trading.krakenapi.rest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.trading.krakenapi.configuration.OpenFeignConfiguration;
import org.trading.krakenapi.rest.dto.KrakenResponse;
import org.trading.krakenapi.rest.dto.WebSocketTokenResponse;

import java.util.Map;

@FeignClient(
    name = "kraken-client",
    url = "https://api.kraken.com",
    configuration = OpenFeignConfiguration.class
)
public interface WebSocketsAuthenticationClient {

    @GetMapping("/0/public/AssetPairs")
    void getTradeableAssetPairs();

    @PostMapping(value = "/0/private/GetWebSocketsToken", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KrakenResponse<WebSocketTokenResponse> getWebsocketsToken(@RequestBody Map<String, ?> form);

    @PostMapping(value = "/0/private/AddOrder", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void addOrder(@RequestBody Map<String, ?> form);
}
