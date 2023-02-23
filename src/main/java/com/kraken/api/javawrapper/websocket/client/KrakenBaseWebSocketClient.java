package com.kraken.api.javawrapper.websocket.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kraken.api.javawrapper.websocket.dto.request.SubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.event.AbstractEventMessage;
import com.kraken.api.javawrapper.websocket.model.event.SystemStatusMessage;
import com.kraken.api.javawrapper.websocket.model.event.request.PingMessage;
import com.kraken.api.javawrapper.websocket.model.event.request.SubscribeMessage;
import com.kraken.api.javawrapper.websocket.model.event.request.UnsubscribeMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.PongMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.SubscriptionStatusMessage;
import com.kraken.api.javawrapper.websocket.model.publication.AbstractPublicationMessage;
import com.kraken.api.javawrapper.websocket.utils.PublicationMessageObjectDeserializer;
import com.kraken.api.javawrapper.websocket.utils.RandomUtils;
import com.kraken.api.javawrapper.websocket.utils.WebSocketTrafficGateway;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kraken.api.javawrapper.properties.KrakenProperties.KRAKEN_REQ_ID_MAX_LIMIT;

@Slf4j
public abstract class KrakenBaseWebSocketClient extends WebSocketClient {
    private final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(
        JsonInclude.Include.NON_NULL
    );
    private final WebSocketTrafficGateway webSocketTrafficGateway = new WebSocketTrafficGateway();

    public KrakenBaseWebSocketClient(final URI krakenWebSocketUrl) {
        super(krakenWebSocketUrl);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.trace("Connection opened with status: {}", serverHandshake.getHttpStatusMessage());
    }

    @Override
    public void onMessage(String s) {
        log.trace("Received message: {}", s);
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(AbstractPublicationMessage.class, new PublicationMessageObjectDeserializer());
        objectMapper.registerModule(simpleModule);
        AbstractEventMessage abstractEventMessage = null;
        AbstractPublicationMessage publicationMessage = null;
        boolean isEventMessage = false;
        try {
            abstractEventMessage = objectMapper.readValue(s, AbstractEventMessage.class);
            isEventMessage = true;
        } catch (JsonProcessingException e) {
            log.trace("Received message is not an AbstractEventMessage. {}", e.getLocalizedMessage());
        }
        if (isEventMessage) {
            if (abstractEventMessage instanceof PongMessage pongMessage) {
                webSocketTrafficGateway.replyAndRemove(pongMessage);
            } else if (abstractEventMessage instanceof SubscriptionStatusMessage subscriptionStatusMessage) {
                webSocketTrafficGateway.reply(subscriptionStatusMessage);
            } else if (abstractEventMessage instanceof SystemStatusMessage systemStatusMessage) {
                webSocketTrafficGateway.getSystemStatusMessages().onNext(systemStatusMessage);
            }
            // else:
            //TODO: Implement default (or passed as parameter) keep-alive max time. Use frequency of HeartbeatMessages
            // to extend max time.
        } else {
            try {
                publicationMessage = objectMapper.readValue(s, AbstractPublicationMessage.class);
                webSocketTrafficGateway.publish(publicationMessage);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Received message is of unknown type. " + ex.getMessage());
            }
        }
        if (Objects.nonNull(abstractEventMessage))
            log.trace("Event message class: {}", abstractEventMessage);
        if (Objects.nonNull(publicationMessage))
            log.trace("Publication message class: {}", publicationMessage);
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

    public Single<PongMessage> ping() {
        return ping(PingMessage.builder()
            .reqId(this.generateRandomReqId())
            .build());
    }

    public Single<PongMessage> ping(PingMessage pingMessage) {
        if (Objects.isNull(pingMessage.getReqId())) pingMessage.setReqId(this.generateRandomReqId());
        ReplaySubject<PongMessage> pongMessageReplaySubject = ReplaySubject.create();
        webSocketTrafficGateway.register(pingMessage.toRequestIdentifier(), pongMessageReplaySubject);
        String pingAsJson;
        try {
            pingAsJson = objectMapper.writeValueAsString(pingMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.send(pingAsJson);
        return pongMessageReplaySubject.firstOrError();
    }

    public List<Single<SubscriptionStatusMessage>> subscribe(SubscribeMessage subscribeMessage) {
        if (Objects.isNull(subscribeMessage.getReqId())) subscribeMessage.setReqId(this.generateRandomReqId());
        List<SubscribeRequestIdentifier> subscribeRequestIdentifiers = subscribeMessage.toRequestIdentifier();
        List<Single<SubscriptionStatusMessage>> list = new ArrayList<>();
        for (SubscribeRequestIdentifier subscribeRequestIdentifier : subscribeRequestIdentifiers) {
            Single<SubscriptionStatusMessage> subscriptionStatusMessageSingle;
            if (!webSocketTrafficGateway.isSubscribed(subscribeRequestIdentifier)) {
                ReplaySubject<SubscriptionStatusMessage> subscriptionStatusMessageReplaySubject = ReplaySubject.create();
                webSocketTrafficGateway.register(subscribeRequestIdentifier, subscriptionStatusMessageReplaySubject);
                ReplaySubject<AbstractPublicationMessage> publicationMessageReplaySubject = webSocketTrafficGateway
                    .subscribe(subscribeRequestIdentifier);
                subscriptionStatusMessageSingle = webSocketTrafficGateway.retrieve(subscribeRequestIdentifier);
                subscriptionStatusMessageSingle = subscriptionStatusMessageSingle.map(e -> {
                    e.setPublicationMessageReplaySubject(publicationMessageReplaySubject);
                    return e;
                });
            } else
                subscriptionStatusMessageSingle = webSocketTrafficGateway.retrieve(subscribeRequestIdentifier);
            list.add(subscriptionStatusMessageSingle);
        }
        String subscribeMessageAsJson;
        try {
            subscribeMessageAsJson = objectMapper.writeValueAsString(subscribeMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.send(subscribeMessageAsJson);

        return list;
    }

    public SubscriptionStatusMessage unsubscribe(UnsubscribeMessage unsubscribeMessage) {

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

    private BigInteger generateRandomReqId() {
        return RandomUtils.nextBigInteger(KRAKEN_REQ_ID_MAX_LIMIT);
    }
}
