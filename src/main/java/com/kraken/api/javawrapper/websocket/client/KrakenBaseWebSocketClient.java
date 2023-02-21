package com.kraken.api.javawrapper.websocket.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kraken.api.javawrapper.websocket.dto.request.SubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.dto.response.SubscribedObject;
import com.kraken.api.javawrapper.websocket.model.event.AbstractEventMessage;
import com.kraken.api.javawrapper.websocket.model.event.SystemStatusMessage;
import com.kraken.api.javawrapper.websocket.model.event.request.PingMessage;
import com.kraken.api.javawrapper.websocket.model.event.request.SubscribeMessage;
import com.kraken.api.javawrapper.websocket.model.event.request.UnsubscribeMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.IResponseMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.PongMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.SubscriptionStatusMessage;
import com.kraken.api.javawrapper.websocket.model.publication.AbstractPublicationMessage;
import com.kraken.api.javawrapper.websocket.utils.PublicationMessageObjectDeserializer;
import com.kraken.api.javawrapper.websocket.utils.RandomUtils;
import com.kraken.api.javawrapper.websocket.utils.WebSocketTrafficGateway;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kraken.api.javawrapper.properties.KrakenProperties.*;

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
        log.info("Connection opened with status: {}", serverHandshake.getHttpStatusMessage());
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
            if (abstractEventMessage instanceof IResponseMessage responseMessage) {
                webSocketTrafficGateway.reply(responseMessage);
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
        PublishSubject<PongMessage> pongMessagePublishSubject = PublishSubject.create();
        webSocketTrafficGateway.register(pingMessage.toRequestIdentifier(), pongMessagePublishSubject);
        String pingAsJson;
        try {
            pingAsJson = objectMapper.writeValueAsString(pingMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.send(pingAsJson);
        return pongMessagePublishSubject.firstOrError();
    }

    public List<SubscribedObject> subscribe(SubscribeMessage subscribeMessage) {
        if (Objects.isNull(subscribeMessage.getReqId())) subscribeMessage.setReqId(this.generateRandomReqId());
        List<SubscribeRequestIdentifier> subscribeRequestIdentifiers = subscribeMessage.toRequestIdentifier();
        List<SubscribedObject> list = new ArrayList<>();
        for (SubscribeRequestIdentifier subscribeRequestIdentifier : subscribeRequestIdentifiers) {
            SubscribeRequestIdentifier subscribeRequestIdentifierForPublication = subscribeRequestIdentifier.toPublicationRequestIdentifier();
            ReplaySubject<AbstractPublicationMessage> abstractPublicationMessageReplaySubject;
            if (webSocketTrafficGateway.isSubscribed(subscribeRequestIdentifierForPublication))
                abstractPublicationMessageReplaySubject = webSocketTrafficGateway.retrieve(subscribeRequestIdentifierForPublication);
            else
                abstractPublicationMessageReplaySubject = webSocketTrafficGateway.subscribe(subscribeRequestIdentifierForPublication);
            PublishSubject<SubscriptionStatusMessage> subscriptionStatusMessagePublishSubject = PublishSubject.create();
            webSocketTrafficGateway.register(subscribeRequestIdentifier, subscriptionStatusMessagePublishSubject);
            list.add(new SubscribedObject(
                subscriptionStatusMessagePublishSubject.firstOrError(),
                abstractPublicationMessageReplaySubject
            ));
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
