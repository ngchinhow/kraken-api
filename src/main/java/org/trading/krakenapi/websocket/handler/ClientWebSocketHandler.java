package org.trading.krakenapi.websocket.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.trading.krakenapi.rest.client.WebSocketsAuthenticationClient;
import org.trading.krakenapi.rest.dto.KrakenResponse;
import org.trading.krakenapi.rest.dto.WebSocketTokenResponse;
import org.trading.krakenapi.websocket.dto.general.*;
import org.trading.krakenapi.websocket.dto.publication.PublicationMessage;
import org.trading.krakenapi.websocket.utils.PublicationMessageObjectDeserializer;

import java.math.BigInteger;
import java.util.*;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ClientWebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientWebSocketHandler.class);
    private PublicationMessageObjectDeserializer publicationMessageObjectDeserializer;
    private final WebSocketsAuthenticationClient webSocketsAuthenticationClient;
    private final List<String> pairs;
    private SubscriptionEmbeddedObject subscriptionEmbeddedObject;

    public ClientWebSocketHandler(
        WebSocketsAuthenticationClient client,
        List<String> pairs,
        SubscriptionEmbeddedObject subscription
    ) {
        this.webSocketsAuthenticationClient = client;
        this.pairs = pairs;
        this.subscriptionEmbeddedObject = subscription;
    }

    @Autowired
    public void setDeserializer(PublicationMessageObjectDeserializer publicationMessageObjectDeserializer) {
        this.publicationMessageObjectDeserializer = publicationMessageObjectDeserializer;
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession webSocketSession) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        KrakenResponse<WebSocketTokenResponse> token = webSocketsAuthenticationClient.getWebsocketsToken(new HashMap<>());
        if (Objects.nonNull(token.getResult()))
            subscriptionEmbeddedObject = subscriptionEmbeddedObject.toBuilder()
                .token(token.getResult().getToken())
                .build();
        SubscribeMessage.SubscribeMessageBuilder<?, ?> subscribeMessageBuilder = SubscribeMessage.builder()
            .subscription(subscriptionEmbeddedObject);
        if (!pairs.isEmpty()) subscribeMessageBuilder.pair(pairs);
        String subscribeAsJson = objectMapper.writeValueAsString(subscribeMessageBuilder.build());
        logger.info("Subscription payload: {}", subscribeAsJson);
        webSocketSession.sendMessage(new TextMessage(subscribeAsJson));
        PingMessage pingMessage = PingMessage.builder()
            .reqid(BigInteger.valueOf(1234))
            .build();
        webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(pingMessage)));
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        logger.info("Received message: {}", message.getPayload());
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(PublicationMessage.class, publicationMessageObjectDeserializer);
        objectMapper.registerModule(simpleModule);
        GeneralMessage generalMessage = null;
        PublicationMessage publicationMessage = null;
        try {
            generalMessage = objectMapper.readValue(message.getPayload(), GeneralMessage.class);
        } catch (MismatchedInputException e) {
            publicationMessage = objectMapper.readValue(message.getPayload(), PublicationMessage.class);
        }
        if (generalMessage != null)
            logger.info("General message class: {}", generalMessage.getClass());
        if (publicationMessage != null)
            logger.info("Publication message class: {}", publicationMessage.getClass());
    }
}
