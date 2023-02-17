package com.kraken.api.javawrapper.manager;

import com.kraken.api.javawrapper.rest.client.RestClient;
import com.kraken.api.javawrapper.rest.client.WebSocketsAuthenticationClient;
import com.kraken.api.javawrapper.rest.enums.RestEnumerations;
import com.kraken.api.javawrapper.rest.requestinterceptor.KrakenRequestInterceptor;
import com.kraken.api.javawrapper.websocket.client.KrakenPrivateWebSocketClient;
import com.kraken.api.javawrapper.websocket.client.KrakenPublicWebSocketClient;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

import static com.kraken.api.javawrapper.properties.KrakenProperties.*;

public class KrakenConnectionManager {
    private final String apiKey;
    private final String privateKey;

    public KrakenConnectionManager(String apiKey, String privateKey) {
        this.apiKey = apiKey;
        this.privateKey = privateKey;
    }

    public <T extends RestClient> T getRestClient(Class<T> tClass) {
        return Feign.builder()
            .client(new OkHttpClient())
            .decoder(new JacksonDecoder())
            .encoder(new JacksonEncoder())
            .logger(new Slf4jLogger())
            .logLevel(Logger.Level.FULL)
            .requestInterceptor(new KrakenRequestInterceptor(apiKey, privateKey))
            .target(tClass, KRAKEN_REST_API_BASE_URI);
    }

    public KrakenPrivateWebSocketClient getKrakenPrivateWebSocketClient() {
        WebSocketsAuthenticationClient client = this.getRestClient(RestEnumerations.ENDPOINT.WEBSOCKETS_AUTHENTICATION);
        return new KrakenPrivateWebSocketClient(client);
    }

    public KrakenPublicWebSocketClient getKrakenPublicWebSocketClient() {
        return new KrakenPublicWebSocketClient();
    }
}
