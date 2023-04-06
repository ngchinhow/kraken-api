package io.github.ngchinhow.kraken.manager;

import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.rest.client.RestClient;
import io.github.ngchinhow.kraken.rest.client.WebSocketsAuthenticationClient;
import io.github.ngchinhow.kraken.rest.requestinterceptor.KrakenRequestInterceptor;
import io.github.ngchinhow.kraken.websocket.client.KrakenPrivateWebSocketClient;
import io.github.ngchinhow.kraken.websocket.client.KrakenPublicWebSocketClient;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import io.github.ngchinhow.kraken.properties.KrakenProperties;
import io.github.ngchinhow.kraken.rest.enums.RestEnumerations;

public final class KrakenConnectionManager {
    private final String apiKey;
    private final String privateKey;

    public KrakenConnectionManager(String apiKey, String privateKey) {
        this.apiKey = apiKey;
        this.privateKey = privateKey;
    }

    public <T extends RestClient> T getRestClient(Class<T> tClass) {
        return Feign.builder()
            .client(new OkHttpClient())
            .decoder(new JacksonDecoder(KrakenProperties.OBJECT_MAPPER))
            .encoder(new JacksonEncoder(KrakenProperties.OBJECT_MAPPER))
            .logger(new Slf4jLogger())
            .logLevel(Logger.Level.FULL)
            .requestInterceptor(new KrakenRequestInterceptor(apiKey, privateKey))
            .target(tClass, KrakenProperties.KRAKEN_REST_API_HOST);
    }

    public KrakenPrivateWebSocketClient getKrakenPrivateWebSocketClient() {
        MarketDataClient marketDataClient = this.getRestClient(RestEnumerations.Endpoint.MARKET_DATA);
        WebSocketsAuthenticationClient webSocketsAuthenticationClient = this.getRestClient(RestEnumerations.Endpoint.WEBSOCKETS_AUTHENTICATION);
        return new KrakenPrivateWebSocketClient(marketDataClient, webSocketsAuthenticationClient);
    }

    public KrakenPublicWebSocketClient getKrakenPublicWebSocketClient() {
        MarketDataClient marketDataClient = this.getRestClient(RestEnumerations.Endpoint.MARKET_DATA);
        return new KrakenPublicWebSocketClient(marketDataClient);
    }
}
