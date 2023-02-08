package com.kraken.api.javawrapper.websocket.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kraken.api.javawrapper.websocket.dto.general.GeneralMessage;
import com.kraken.api.javawrapper.websocket.dto.general.PingMessage;
import com.kraken.api.javawrapper.websocket.dto.general.SubscribeMessage;
import com.kraken.api.javawrapper.websocket.dto.general.SubscriptionEmbeddedObject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import com.kraken.api.javawrapper.websocket.dto.publication.PublicationMessage;
import com.kraken.api.javawrapper.websocket.utils.PublicationMessageObjectDeserializer;

import java.math.BigInteger;
import java.net.URI;
import java.util.*;

@Slf4j
public class KrakenWebSocketClient extends WebSocketClient {
    private final List<String> pairs;
    private final SubscriptionEmbeddedObject subscriptionEmbeddedObject;

    public KrakenWebSocketClient(URI uri, List<String> pairs, SubscriptionEmbeddedObject subscription) {
        super(uri);
        this.pairs = pairs;
        this.subscriptionEmbeddedObject = subscription;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SubscribeMessage.SubscribeMessageBuilder<?, ?> subscribeMessageBuilder = SubscribeMessage.builder()
            .subscription(subscriptionEmbeddedObject);
        if (!pairs.isEmpty()) subscribeMessageBuilder.pair(pairs);
        PingMessage pingMessage = PingMessage.builder()
            .reqid(BigInteger.valueOf(1234))
            .build();
        String subscribeAsJson;
        String pingAsJson;
        try {
            subscribeAsJson = objectMapper.writeValueAsString(subscribeMessageBuilder.build());
            pingAsJson = objectMapper.writeValueAsString(pingMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("Subscription payload: {}", subscribeAsJson);
        this.send(subscribeAsJson);
        this.send(pingAsJson);
    }

    @Override
    public void onMessage(String s) {
        log.info("Received message: {}", s);
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(PublicationMessage.class, new PublicationMessageObjectDeserializer());
        objectMapper.registerModule(simpleModule);
        GeneralMessage generalMessage = null;
        PublicationMessage publicationMessage = null;
        try {
            generalMessage = objectMapper.readValue(s, GeneralMessage.class);
        } catch (JsonProcessingException e) {
            try {
                publicationMessage = objectMapper.readValue(s, PublicationMessage.class);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (generalMessage != null)
            log.info("General message class: {}", generalMessage.getClass());
        if (publicationMessage != null)
            log.info("Publication message class: {}", publicationMessage.getClass());
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.trace("Connection closed. i: {}, s: {}, b: {}", i, s, b);
    }

    @Override
    public void onError(Exception e) {
        log.error("Unexpected error during websocket operation: {}", e.getMessage());
        throw new RuntimeException(e);
    }
}
