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
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import lombok.Getter;
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
@Getter
public abstract class KrakenBaseWebSocketClient extends WebSocketClient {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebSocketTrafficGateway webSocketTrafficGateway = new WebSocketTrafficGateway();

    public KrakenBaseWebSocketClient(final URI krakenWebSocketUrl) {
        super(krakenWebSocketUrl);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(AbstractPublicationMessage.class, new PublicationMessageObjectDeserializer());
        objectMapper.registerModule(simpleModule);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.trace("Connection opened with status: {}", serverHandshake.getHttpStatusMessage());
    }

    @Override
    public void onMessage(String s) {
        log.trace("Received message: {}", s);
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
                webSocketTrafficGateway.responseReplyAndRemove(pongMessage);
            } else if (abstractEventMessage instanceof SubscriptionStatusMessage subscriptionStatusMessage) {
                webSocketTrafficGateway.responseReply(subscriptionStatusMessage);
            } else if (abstractEventMessage instanceof SystemStatusMessage systemStatusMessage) {
                webSocketTrafficGateway.responseAnnounce(systemStatusMessage);
            }
            //TODO: Implement default (or passed as parameter) keep-alive max time. Use frequency of HeartbeatMessages
            // to extend max time.
            // else:
        } else {
            try {
                publicationMessage = objectMapper.readValue(s, AbstractPublicationMessage.class);
                webSocketTrafficGateway.publishMessage(publicationMessage);
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
        return ping(new PingMessage());
    }

    public Single<PongMessage> ping(PingMessage pingMessage) {
        if (Objects.isNull(pingMessage.getReqId())) pingMessage.setReqId(this.generateRandomReqId());
        ReplaySubject<PongMessage> pongMessageReplaySubject = ReplaySubject.create();
        webSocketTrafficGateway.registerRequest(pingMessage.toRequestIdentifier(), pongMessageReplaySubject);
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

        List<SubscribeRequestIdentifier> subscribeRequestIdentifiers = subscribeMessage.toRequestIdentifiers();
        List<Single<SubscriptionStatusMessage>> list = new ArrayList<>();
        List<String> subscribedToBeRemoved = new ArrayList<>(subscribeMessage.getPairs());
        for (SubscribeRequestIdentifier subscribeRequestIdentifier : subscribeRequestIdentifiers) {
            PublishSubject<AbstractPublicationMessage> publicationMessagePublishSubject;
            if (webSocketTrafficGateway.isRequestSubscribed(subscribeRequestIdentifier)) {
                /*
                  CRITICAL:
                  Within the same connection, subscribing using the same payload is allowed, and a
                  SubscriptionStatusMessage is returned. When subscribing to the BOOK channel, a BookSnapshotMessage
                  is received, but not for all repeated subscriptions. Since the AbstractPublicationMessages have no
                  information that ties them to a particular SubscriptionStatusMessage, they cannot be directed to a
                  particular ReplaySubject. Therefore, these duplicate subscriptions will be disallowed to be performed
                  in the first place; they are removed from the SubscribeMessage. User will be unaware of this; they
                  will receive the SubscriptionStatusMessage received on the first subscription.
                 */
                log.debug(
                    "The set of inputs " +
                        "pair: {}, " +
                        "channel: {}, " +
                        "depth: {}, " +
                        "interval: {}, " +
                        "ratecounter: {}, " +
                        "snapshot: {} and " +
                        "consolidate_taker: {} " +
                        "has already been subscribed to. Removing pair from SubscribeMessage",
                    subscribeRequestIdentifier.getPair(),
                    subscribeMessage.getSubscription().getName().getVChannel(),
                    subscribeMessage.getSubscription().getDepth(),
                    subscribeMessage.getSubscription().getInterval(),
                    subscribeMessage.getSubscription().getRateCounter(),
                    subscribeMessage.getSubscription().getSnapshot(),
                    subscribeMessage.getSubscription().getConsolidateTaker()
                );
                subscribedToBeRemoved.remove(subscribeRequestIdentifier.getPair());
                publicationMessagePublishSubject = webSocketTrafficGateway.getSubscriptionSubject(subscribeRequestIdentifier);
            } else {
                ReplaySubject<SubscriptionStatusMessage> subscriptionStatusMessageReplaySubject = ReplaySubject.create();
                webSocketTrafficGateway.registerRequest(subscribeRequestIdentifier, subscriptionStatusMessageReplaySubject);
                publicationMessagePublishSubject = webSocketTrafficGateway.subscribeRequest(subscribeRequestIdentifier);
            }
            Single<SubscriptionStatusMessage> subscriptionStatusMessageSingle = webSocketTrafficGateway.retrieveResponse(subscribeRequestIdentifier);
            subscriptionStatusMessageSingle = subscriptionStatusMessageSingle.map(e -> {
                e.setPublicationMessagePublishSubject(publicationMessagePublishSubject);
                return e;
            });
            list.add(subscriptionStatusMessageSingle);
        }
        subscribeMessage.setPairs(subscribedToBeRemoved);
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
