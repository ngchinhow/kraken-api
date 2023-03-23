package com.kraken.api.javawrapper.websocket.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kraken.api.javawrapper.websocket.dto.request.SubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.dto.request.UnsubscribeRequestIdentifier;
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
        if (Objects.nonNull(abstractEventMessage)) log.trace("Event message class: {}", abstractEventMessage);
        if (Objects.nonNull(publicationMessage)) log.trace("Publication message class: {}", publicationMessage);
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

    /**
     * Subscription to a public or private channel via the Subscribe payload.
     * <p>
     * Behaviour caveats:
     * <ol>
     *   <li>
     *     A RxJava PublishSubject is used for the messages of the subscription. This is due to its behaviour: only
     *     messages after the retrieval request is sent, i.e. "get" related methods, are returned to the user. This
     *     ensure the user always has the latest information of the subscribed channel.
     *   </li>
     *   <li>
     *     If the same SubscribeMessage is sent twice or more to the same connection/client, the second and every
     *     subsequent one is ignored **for the same input parameters**. This is true for payloads whereby the difference
     *     is only the reqId as well. This is because of the ambiguous behaviour of messages returned from their
     *     websockets:
     *     <ul>
     *       <li>
     *         Subscriptions on the same parameters is allowed. Messages may or may not be repetitively pushed into the
     *         same websocket.
     *       </li>
     *       <li>
     *         A BookSnapshotMessage is sent on some but not all successful subscription to the BOOK channel, even for
     *         repeated subscriptions.
     *       </li>
     *     </ul>
     *     While it is not inherently wrong to allow multiple of the same subscription, the lack of information on these
     *     messages mean they cannot be correlated back to the original SubscriptionStatusMessage, and therefore its
     *     PublishSubject.
     * <p>
     *     Therefore, any repeated subscription payloads will be ignored; specifically, that pair will be removed from
     *     the payload. A SubscriptionStatusMessage will still be returned to the user for that pair, but it will be
     *     for when the first subscription happened.
     *   </li>
     * </ol>
     *
     * @param subscribeMessage The subscription payload from the user
     * @return List of Single of SubscriptionStatusMessages
     * @see <a href="https://reactivex.io/documentation/subject.html">ReactiveX Subject</a>
     * @see <a href="https://docs.kraken.com/websockets/#message-subscribe">Kraken Subscribe</a>
     */
    public List<Single<SubscriptionStatusMessage>> subscribe(SubscribeMessage subscribeMessage) {
        if (Objects.isNull(subscribeMessage.getReqId())) subscribeMessage.setReqId(this.generateRandomReqId());

        List<SubscribeRequestIdentifier> subscribeRequestIdentifiers = subscribeMessage.toRequestIdentifiers();
        List<Single<SubscriptionStatusMessage>> list = new ArrayList<>();
        List<String> subscribedToBeRemoved = new ArrayList<>(subscribeMessage.getPairs());
        for (SubscribeRequestIdentifier subscribeRequestIdentifier : subscribeRequestIdentifiers) {
            PublishSubject<AbstractPublicationMessage> publicationMessagePublishSubject;
            if (webSocketTrafficGateway.isRequestSubscribed(subscribeRequestIdentifier)) {
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
            Single<SubscriptionStatusMessage> subscriptionStatusMessageSingle = webSocketTrafficGateway
                .retrieveResponse(subscribeRequestIdentifier);
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

    public List<Single<SubscriptionStatusMessage>> unsubscribe(UnsubscribeMessage unsubscribeMessage) {
        if (Objects.isNull(unsubscribeMessage.getReqId())) unsubscribeMessage.setReqId(this.generateRandomReqId());

        List<UnsubscribeRequestIdentifier> unsubscribeRequestIdentifiers = unsubscribeMessage.toRequestIdentifiers();
        List<Single<SubscriptionStatusMessage>> list = new ArrayList<>();
//        List<String> subscribedToBeRemoved = new ArrayList<>(unsubscribeMessage.getPairs());
        for (UnsubscribeRequestIdentifier unsubscribeRequestIdentifier : unsubscribeRequestIdentifiers) {
            ReplaySubject<SubscriptionStatusMessage> subscriptionStatusMessageReplaySubject = ReplaySubject.create();
            webSocketTrafficGateway.registerRequest(unsubscribeRequestIdentifier, subscriptionStatusMessageReplaySubject);
            list.add(webSocketTrafficGateway.retrieveResponse(unsubscribeRequestIdentifier));
        }

        String unsubscribeMessageAsJson;
        try {
            unsubscribeMessageAsJson = objectMapper.writeValueAsString(unsubscribeMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.send(unsubscribeMessageAsJson);
        return list;
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
