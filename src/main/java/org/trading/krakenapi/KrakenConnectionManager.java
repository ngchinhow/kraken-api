package org.trading.krakenapi;

import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.trading.krakenapi.rest.client.*;
import org.trading.krakenapi.rest.enums.RestEnumerations;
import org.trading.krakenapi.websocket.enums.WebSocketEnumerations;
import org.trading.krakenapi.websocket.handler.ClientWebSocketHandler;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@Import(FeignClientsConfiguration.class)
public class KrakenConnectionManager {
    private ObjectProvider<RequestInterceptor> requestInterceptorObjectProvider;
    private ObjectProvider<ClientWebSocketHandler> clientWebSocketHandlerObjectProvider;
    private Decoder decoder;
    private Encoder encoder;
    private Contract contract;
    private Client client;
    private String baseUri;

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
        @Value("${kraken.rest-api.base-uri}") String baseUri
    ) {
        this.requestInterceptorObjectProvider = requestInterceptorObjectProvider;
        this.clientWebSocketHandlerObjectProvider = clientWebSocketHandlerObjectProvider;
        this.decoder = decoder;
        this.encoder = feignFormEncoder;
        this.contract = contract;
        this.client = client;
        this.baseUri = baseUri;
    }

    public RestClient getRestClient(RestEnumerations.ENDPOINT endpoint) {
        RequestInterceptor requestInterceptor = requestInterceptorObjectProvider.getObject(apiKey, privateKey);
        return Feign.builder()
            .client(client)
            .decoder(decoder)
            .encoder(encoder)
            .contract(contract)
            .requestInterceptor(requestInterceptor)
            .target(endpoint.getRestClientClass(), baseUri);
    }

    public WebSocketConnectionManager getWebsocketConnectionManager(WebSocketEnumerations.CHANNEL channel, String pair) {
        ClientWebSocketHandler clientWebSocketHandler = clientWebSocketHandlerObjectProvider
            .getObject(channel, Collections.singletonList(pair));
        return new WebSocketConnectionManager(
            new StandardWebSocketClient(),
            clientWebSocketHandler,
            baseUri
        );
    }

    public WebSocketConnectionManager getWebsocketConnectionManager(
        WebSocketEnumerations.CHANNEL channel,
        List<String> pairs
    ) {
        ClientWebSocketHandler clientWebSocketHandler = clientWebSocketHandlerObjectProvider.getObject(channel, pairs);
        return new WebSocketConnectionManager(
            new StandardWebSocketClient(),
            clientWebSocketHandler,
            baseUri
        );
    }
}
