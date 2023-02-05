package org.trading.krakenapi.manager;

import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.trading.krakenapi.properties.KrakenProperties;
import org.trading.krakenapi.rest.client.*;
import org.trading.krakenapi.rest.enums.RestEnumerations;
import org.trading.krakenapi.websocket.dto.general.SubscriptionEmbeddedObject;
import org.trading.krakenapi.websocket.enums.WebSocketEnumerations;
import org.trading.krakenapi.websocket.handler.ClientWebSocketHandler;

import java.util.Collections;
import java.util.List;

@Lazy
@Service
@Getter
@Setter
@Import(FeignClientsConfiguration.class)
public class KrakenConnectionManager {
    private ObjectProvider<RequestInterceptor> requestInterceptorObjectProvider;
    private ObjectProvider<ClientWebSocketHandler> clientWebSocketHandlerObjectProvider;
    private Decoder decoder;
    private Encoder encoder;
    private Contract contract;
    private Client client;
    private KrakenProperties krakenProperties;


    private final String apiKey;
    private final String privateKey;

    public KrakenConnectionManager(String apiKey, String privateKey) {
        this.apiKey = apiKey;
        this.privateKey = privateKey;
    }

    @Autowired
    public void setBeans(
        ObjectProvider<RequestInterceptor> requestInterceptorObjectProvider,
        ObjectProvider<ClientWebSocketHandler> clientWebSocketHandlerObjectProvider,
        Decoder decoder,
        Encoder feignFormEncoder,
        Contract contract,
        Client client,
        KrakenProperties krakenProperties
    ) {
        this.requestInterceptorObjectProvider = requestInterceptorObjectProvider;
        this.clientWebSocketHandlerObjectProvider = clientWebSocketHandlerObjectProvider;
        this.decoder = decoder;
        this.encoder = feignFormEncoder;
        this.contract = contract;
        this.client = client;
        this.krakenProperties = krakenProperties;
    }

    public RestClient getRestClient(RestEnumerations.ENDPOINT endpoint) {
        RequestInterceptor requestInterceptor = requestInterceptorObjectProvider.getObject(apiKey, privateKey);
        return Feign.builder()
            .client(client)
            .decoder(decoder)
            .encoder(encoder)
            .contract(contract)
            .requestInterceptor(requestInterceptor)
            .target(endpoint.getRestClientClass(), krakenProperties.getRest().getBaseUri());
    }

    public WebSocketConnectionManager getWebsocketConnectionManager(String pair, SubscriptionEmbeddedObject subscription) {
        return this.getWebsocketConnectionManager(Collections.singletonList(pair), subscription);
    }

    public WebSocketConnectionManager getWebsocketConnectionManager(
        List<String> pairs,
        SubscriptionEmbeddedObject subscription
    ) {
        WebSocketsAuthenticationClient webSocketsAuthenticationClient =
            (WebSocketsAuthenticationClient) this.getRestClient(RestEnumerations.ENDPOINT.WEBSOCKET_AUTHENTICATION);
        ClientWebSocketHandler clientWebSocketHandler = clientWebSocketHandlerObjectProvider
            .getObject(webSocketsAuthenticationClient, pairs, subscription);
        String url = (
            subscription.getName().equals(WebSocketEnumerations.CHANNEL.OWN_TRADES) ||
            subscription.getName().equals(WebSocketEnumerations.CHANNEL.OPEN_ORDERS)
        ) ? krakenProperties.getWebsocket().getPrivateUrl() : krakenProperties.getWebsocket().getPublicUrl();
        return new WebSocketConnectionManager(new StandardWebSocketClient(), clientWebSocketHandler, url);
    }
}
