package com.kraken.api.javawrapper.manager;

import com.kraken.api.javawrapper.rest.client.MarketDataClient;
import com.kraken.api.javawrapper.rest.client.RestClient;
import com.kraken.api.javawrapper.rest.client.WebSocketsAuthenticationClient;
import com.kraken.api.javawrapper.rest.requestinterceptor.KrakenRequestInterceptor;
import com.kraken.api.javawrapper.websocket.client.KrakenPrivateWebSocketClient;
import com.kraken.api.javawrapper.websocket.client.KrakenPublicWebSocketClient;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

import static com.kraken.api.javawrapper.properties.KrakenProperties.KRAKEN_REST_API_BASE_URI;
import static com.kraken.api.javawrapper.properties.KrakenProperties.OBJECT_MAPPER;
import static com.kraken.api.javawrapper.rest.enums.RestEnumerations.Endpoint.MARKET_DATA;
import static com.kraken.api.javawrapper.rest.enums.RestEnumerations.Endpoint.WEBSOCKETS_AUTHENTICATION;

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
            .decoder(new JacksonDecoder(OBJECT_MAPPER))
            .encoder(new JacksonEncoder(OBJECT_MAPPER))
            .logger(new Slf4jLogger())
            .logLevel(Logger.Level.FULL)
            .requestInterceptor(new KrakenRequestInterceptor(apiKey, privateKey))
            .target(tClass, KRAKEN_REST_API_BASE_URI);
    }

    public KrakenPrivateWebSocketClient getKrakenPrivateWebSocketClient() {
        MarketDataClient marketDataClient = this.getRestClient(MARKET_DATA);
        WebSocketsAuthenticationClient webSocketsAuthenticationClient = this.getRestClient(WEBSOCKETS_AUTHENTICATION);
        return new KrakenPrivateWebSocketClient(marketDataClient, webSocketsAuthenticationClient);
    }

    public KrakenPublicWebSocketClient getKrakenPublicWebSocketClient() {
        MarketDataClient marketDataClient = this.getRestClient(MARKET_DATA);
        return new KrakenPublicWebSocketClient(marketDataClient);
    }
}
