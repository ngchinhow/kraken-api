package com.kraken.api.javawrapper.manager;

import com.kraken.api.javawrapper.rest.client.RestClient;
import com.kraken.api.javawrapper.rest.client.WebSocketsAuthenticationClient;
import com.kraken.api.javawrapper.rest.dto.KrakenResponse;
import com.kraken.api.javawrapper.rest.dto.websocketsauthentication.WebSocketsTokenResult;
import com.kraken.api.javawrapper.rest.enums.RestEnumerations;
import com.kraken.api.javawrapper.rest.requestinterceptor.KrakenRequestInterceptor;
import com.kraken.api.javawrapper.websocket.client.KrakenWebSocketClient;
import com.kraken.api.javawrapper.websocket.model.event.embedded.SubscriptionEmbeddedObject;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public KrakenWebSocketClient getKrakenWebSocketClient(String pair, SubscriptionEmbeddedObject subscription) {
        return this.getKrakenWebSocketClient(Collections.singletonList(pair), subscription);
    }

    public KrakenWebSocketClient getKrakenWebSocketClient(List<String> pairs, SubscriptionEmbeddedObject subscription) {
        final boolean isPrivateSubscription = subscription.getName().equals(WebSocketEnumerations.CHANNEL.OWN_TRADES) ||
            subscription.getName().equals(WebSocketEnumerations.CHANNEL.OPEN_ORDERS);
        final URI uri = isPrivateSubscription ? KRAKEN_WEBSOCKET_API_PRIVATE_URL : KRAKEN_WEBSOCKET_API_PUBLIC_URL;
        if (isPrivateSubscription) {
            WebSocketsAuthenticationClient client = this.getRestClient(RestEnumerations.ENDPOINT.WEBSOCKETS_AUTHENTICATION);
            KrakenResponse<WebSocketsTokenResult> tokenResponse = client.getWebsocketsToken();
            if (Objects.nonNull(tokenResponse.getResult()))
                subscription = subscription.toBuilder()
                    .token(tokenResponse.getResult().getToken())
                    .build();
        }
        return new KrakenWebSocketClient(uri, pairs, subscription);
    }
}
