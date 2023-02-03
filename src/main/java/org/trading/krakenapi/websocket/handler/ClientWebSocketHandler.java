package org.trading.krakenapi.websocket.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jetbrains.annotations.NotNull;
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
import org.trading.krakenapi.websocket.dto.general.GeneralMessage;
import org.trading.krakenapi.websocket.dto.general.PingMessage;
import org.trading.krakenapi.websocket.dto.general.SubscribeMessage;
import org.trading.krakenapi.websocket.dto.general.SubscriptionEmbeddedObject;
import org.trading.krakenapi.websocket.dto.publication.PublicationMessage;
import org.trading.krakenapi.websocket.dto.publication.TickerMessage;
import org.trading.krakenapi.websocket.enums.WebSocketEnumerations;
import org.trading.krakenapi.websocket.utils.PublicationMessageObjectDeserializer;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ClientWebSocketHandler extends TextWebSocketHandler {
    private PublicationMessageObjectDeserializer publicationMessageObjectDeserializer;
    private WebSocketsAuthenticationClient webSocketsAuthenticationClient;
    private final WebSocketEnumerations.CHANNEL channel;
    private final String pair;

    public ClientWebSocketHandler(WebSocketEnumerations.CHANNEL channel, String pair) {
        this.channel = channel;
        this.pair = pair;
    }

    @Autowired
    public void setDeserializer(PublicationMessageObjectDeserializer publicationMessageObjectDeserializer) {
        this.publicationMessageObjectDeserializer = publicationMessageObjectDeserializer;
    }

    @Autowired
    public void setWebSocketsAuthenticationClient(WebSocketsAuthenticationClient client) {
        this.webSocketsAuthenticationClient = client;
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession webSocketSession) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Map<String, String> params = new HashMap<>();
        KrakenResponse<WebSocketTokenResponse> token = webSocketsAuthenticationClient.getWebsocketsToken(params);
        SubscriptionEmbeddedObject.Builder embeddedObjectBuilder = SubscriptionEmbeddedObject.builder().name(channel);
        if (Objects.nonNull(token.getResult()))
            embeddedObjectBuilder = embeddedObjectBuilder.token(token.getResult().getToken());
        SubscribeMessage subscribeMessage = SubscribeMessage.builder()
            .pair(pair)
            .subscription(embeddedObjectBuilder.build())
            .build();
        String subscribeAsJson = objectMapper.writeValueAsString(subscribeMessage);
        webSocketSession.sendMessage(new TextMessage(subscribeAsJson));
        PingMessage pingMessage = PingMessage.builder()
            .reqid(BigInteger.valueOf(1234))
            .build();
        webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(pingMessage)));
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message.getPayload());
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
            System.out.println(generalMessage.getClass());
        if (publicationMessage != null)
            System.out.println(((TickerMessage) publicationMessage).getAsk());
//        System.out.println(message.getPayload());
//        System.out.println(objectMapper.readValue(message.getPayload(), GeneralMessage.class).getClass());
//        ReflectionUtils.doWithFields(BookMessage.class, field -> System.out.println(field.getName()));
    }
}
