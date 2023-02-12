package com.kraken.api.javawrapper.websocket.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kraken.api.javawrapper.websocket.dto.RequestIdentifier;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import com.kraken.api.javawrapper.websocket.model.event.*;
import com.kraken.api.javawrapper.websocket.model.event.embedded.SubscriptionEmbeddedObject;
import com.kraken.api.javawrapper.websocket.model.publication.AbstractPublicationMessage;
import com.kraken.api.javawrapper.websocket.model.event.request.PingMessage;
import com.kraken.api.javawrapper.websocket.model.event.request.SubscribeMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.PongMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.SubscriptionStatusMessage;
import com.kraken.api.javawrapper.websocket.utils.PublicationMessageObjectDeserializer;
import com.kraken.api.javawrapper.websocket.utils.ConcurrentLinkedQueueUtils;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.math.BigInteger;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class KrakenWebSocketClient extends WebSocketClient {
    private final Map<? extends RequestIdentifier, ConcurrentLinkedQueue<? extends AbstractInteractiveMessage>>
        requestsToQueueMap = new HashMap<>();

    private final List<String> pairs;
    private final SubscriptionEmbeddedObject subscriptionEmbeddedObject;

    public KrakenWebSocketClient(URI uri, List<String> pairs, SubscriptionEmbeddedObject subscription) {
        super(uri);
        this.pairs = pairs;
        this.subscriptionEmbeddedObject = subscription;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        Map<Class<? extends AbstractEventMessage>, ConcurrentLinkedQueue<? extends AbstractEventMessage>> hashMap =
            Arrays.stream(WebSocketEnumerations.EVENT.class.getDeclaredClasses()).map(c -> {
            try {
                //noinspection unchecked
                return (Class<? extends AbstractEventMessage>) c.getField("").get(null);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toMap(Function.identity(), ConcurrentLinkedQueueUtils::create));
        ConcurrentLinkedQueue<SubscribeMessage> t = (ConcurrentLinkedQueue<SubscribeMessage>) hashMap.get(SubscribeMessage.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SubscribeMessage.SubscribeMessageBuilder<?, ?> subscribeMessageBuilder = SubscribeMessage.builder()
            .subscription(subscriptionEmbeddedObject);
        if (!pairs.isEmpty()) subscribeMessageBuilder.pair(pairs);
        PingMessage pingMessage = PingMessage.builder()
            .reqId(BigInteger.valueOf(1234))
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
//        this.send(pingAsJson);
        try {
            subscribeAsJson = objectMapper.writeValueAsString(subscribeMessageBuilder
                .reqId(BigInteger.valueOf(123))
                .subscription(subscriptionEmbeddedObject.toBuilder().depth(100).build())
                .build()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("Subscription payload: {}", subscribeAsJson);
        this.send(subscribeAsJson);
//        UnsubscribeMessage unsubscribeMessage = UnsubscribeMessage.builder()
//            .subscription(subscriptionEmbeddedObject)
//            .pair(pairs)
//            .build();
//        String unsubscribeAsJson;
//        try {
//            unsubscribeAsJson = objectMapper.writeValueAsString(unsubscribeMessage);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        log.info("Unsubscription payload: {}", unsubscribeAsJson);
//        this.send(unsubscribeAsJson);
    }

    @Override
    public void onMessage(String s) {
        log.info("Received message: {}", s);
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(AbstractPublicationMessage.class, new PublicationMessageObjectDeserializer());
        objectMapper.registerModule(simpleModule);
        AbstractEventMessage abstractEventMessage = null;
        AbstractPublicationMessage publicationMessage = null;
        boolean isNotGeneralMessage = true;
        try {
            abstractEventMessage = objectMapper.readValue(s, AbstractEventMessage.class);
            isNotGeneralMessage = false;
        } catch (JsonProcessingException e) {
            log.trace("Received message is not an AbstractEventMessage. {}", e.getLocalizedMessage());
        }
        if (isNotGeneralMessage) {
            try {
                publicationMessage = objectMapper.readValue(s, AbstractPublicationMessage.class);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Received message is of unknown type. " + ex.getMessage());
            }
        }
        if (Objects.nonNull(abstractEventMessage))
            log.info("General message class: {}", abstractEventMessage);
        if (Objects.nonNull(publicationMessage))
            log.info("Publication message class: {}", publicationMessage);
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

    public PongMessage ping() {

        return null;
    }

    public SubscriptionStatusMessage subscribe() {

        return null;
    }

    public SubscriptionStatusMessage unsubscribe() {

        return null;
    }

    public void addOrder() {

    }

    public void editOrder() {

    }

    public void cancelOrder() {

    }

    public void cancelAllOrders() {

    }

    public void cancelAllOrdersAfter() {

    }
}
