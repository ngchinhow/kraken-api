package com.kraken.api.javawrapper.websocket.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kraken.api.javawrapper.rest.client.MarketDataClient;
import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.message.AbstractMessage;
import com.kraken.api.javawrapper.websocket.model.message.AbstractPublicationMessage;
import com.kraken.api.javawrapper.websocket.model.message.HeartbeatMessage;
import com.kraken.api.javawrapper.websocket.model.message.StatusMessage;
import com.kraken.api.javawrapper.websocket.model.method.*;
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
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kraken.api.javawrapper.properties.KrakenProperties.KRAKEN_REQ_ID_MAX_LIMIT;
import static com.kraken.api.javawrapper.properties.KrakenProperties.OBJECT_MAPPER;

@Slf4j
@Getter
public abstract class KrakenBaseWebSocketClient extends WebSocketClient {
    private final WebSocketTrafficGateway webSocketTrafficGateway = new WebSocketTrafficGateway();
    private final MarketDataClient marketDataClient;

    public KrakenBaseWebSocketClient(final URI krakenWebSocketUrl, MarketDataClient marketDataClient) {
        super(krakenWebSocketUrl);
        this.marketDataClient = marketDataClient;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.trace("Connection opened with status: {}", serverHandshake.getHttpStatusMessage());
    }

    @Override
    public void onMessage(String s) {
        log.trace("Received message: {}", s);
        AbstractResponse abstractResponse = null;
        AbstractMessage abstractMessage = null;
        boolean isResponse = false;
        boolean isMessage = false;
        try {
            abstractResponse = OBJECT_MAPPER.readValue(s, AbstractResponse.class);
            isResponse = true;
        } catch (JsonProcessingException e) {
            log.trace("Received message is not an AbstractResponse. {}", e.getLocalizedMessage());
        }
        if (isResponse) {
            if (abstractResponse instanceof Interaction.AbstractInteractionResponse interactionResponse &&
                !interactionResponse.getSuccess()) {
                log.error(interactionResponse.getError());
                webSocketTrafficGateway.removeErrorRequest(abstractResponse);
            } else
                webSocketTrafficGateway.responseReply(abstractResponse);
        } else {
            try {
                abstractMessage = OBJECT_MAPPER.readValue(s, AbstractMessage.class);
                isMessage = true;
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Received message is of unknown type. " + ex.getMessage());
            }
        }
        if (isMessage) {
            if (abstractMessage instanceof HeartbeatMessage heartbeatMessage) {
                //TODO: Implement default (or passed as parameter) keep-alive max time. Use frequency of HeartbeatMessages
                // to extend max time.
            } else if (abstractMessage instanceof StatusMessage statusMessage) {
                webSocketTrafficGateway.responseAnnounce(statusMessage);
            } else {
                AbstractPublicationMessage publicationMessage = (AbstractPublicationMessage) abstractMessage;
                webSocketTrafficGateway.publishMessage(publicationMessage);
            }
        }
        if (Objects.nonNull(abstractResponse)) log.trace("Response class: {}", abstractResponse);
        if (Objects.nonNull(abstractMessage)) log.trace("Message class: {}", abstractMessage);
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

    public Single<Echo.PongResponse> ping() {
        return ping(new Echo.PingRequest());
    }

    public Single<Echo.PongResponse> ping(Echo.PingRequest pingRequest) {
        if (Objects.isNull(pingRequest.getRequestId())) pingRequest.setRequestId(this.generateRandomReqId());
        ZonedDateTime serverTime = marketDataClient.getServerTime().getResult().getIsoTime();
        // PingRequests do not have any symbols involved, so there is only one entry - the one associated with null
        RequestIdentifier requestIdentifier = pingRequest.toRequestIdentifier(serverTime);
        ReplaySubject<Echo.PongResponse> pongMessageReplaySubject = ReplaySubject.create();
        webSocketTrafficGateway.registerRequest(requestIdentifier, pongMessageReplaySubject);
        this.sendPayload(pingRequest, serverTime);
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
     * <p>
     * //     * @param subscribeRequest The subscription payload from the user
     *
     * @return List of Single of SubscriptionStatusMessages
     * @see <a href="https://reactivex.io/documentation/subject.html">ReactiveX Subject</a>
     * @see <a href="https://docs.kraken.com/websockets/#message-subscribe">Kraken Subscribe</a>
     */
    public List<Single<Subscription.SubscribeResponse>> subscribe(Subscription.SubscribeRequest subscribeRequest) {
        if (Objects.isNull(subscribeRequest.getRequestId())) subscribeRequest.setRequestId(this.generateRandomReqId());
        ZonedDateTime serverTime = marketDataClient.getServerTime().getResult().getIsoTime();
        List<RequestIdentifier> requestIdentifiers = subscribeRequest.toRequestIdentifiers(serverTime);
        List<Single<Subscription.SubscribeResponse>> list = new ArrayList<>();
        for (RequestIdentifier requestIdentifier : requestIdentifiers) {
            ReplaySubject<Subscription.SubscribeResponse> subscribeResponseReplaySubject = ReplaySubject.create(1);
            webSocketTrafficGateway.registerRequest(requestIdentifier, subscribeResponseReplaySubject);
            // Publication messages do not have req_id or timestamp information, so they must be removed from the
            // identifier
            RequestIdentifier publicationRequestIdentifier = requestIdentifier.duplicate();
            PublishSubject<AbstractPublicationMessage> publicationMessagePublishSubject = webSocketTrafficGateway
                .subscribePublication(publicationRequestIdentifier);
            Single<Subscription.SubscribeResponse> subscribeResponseSingle = webSocketTrafficGateway
                .retrieveResponse(requestIdentifier);
            subscribeResponseSingle = subscribeResponseSingle.map(e -> {
                e.setPublicationMessagePublishSubject(publicationMessagePublishSubject);
                return e;
            });
            list.add(subscribeResponseSingle);
        }
        this.sendPayload(subscribeRequest, serverTime);
        return list;
    }

//    public List<Single<SubscriptionStatusMessage>> unsubscribe(UnsubscribeRequestMessage unsubscribeRequestMessage) {
//        if (Objects.isNull(unsubscribeRequestMessage.getReqId())) unsubscribeRequestMessage.setReqId(this.generateRandomReqId());
//
//        List<UnsubscribeRequestIdentifier> unsubscribeRequestIdentifiers = unsubscribeRequestMessage.toRequestIdentifiers();
//        List<Single<SubscriptionStatusMessage>> list = new ArrayList<>();
////        List<String> subscribedToBeRemoved = new ArrayList<>(unsubscribeMessage.getPairs());
//        for (UnsubscribeRequestIdentifier unsubscribeRequestIdentifier : unsubscribeRequestIdentifiers) {
//            ReplaySubject<SubscriptionStatusMessage> subscriptionStatusMessageReplaySubject = ReplaySubject.create();
//            webSocketTrafficGateway.registerRequest(unsubscribeRequestIdentifier, subscriptionStatusMessageReplaySubject);
//            list.add(webSocketTrafficGateway.retrieveResponse(unsubscribeRequestIdentifier));
//        }
//
//        String unsubscribeMessageAsJson;
//        try {
//            unsubscribeMessageAsJson = objectMapper.writeValueAsString(unsubscribeRequestMessage);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        this.send(unsubscribeMessageAsJson);
//        return list;
//    }

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

    private <T extends AbstractRequest> void sendPayload(T request, ZonedDateTime timestamp) {
        String requestAsJson;
        try {
            requestAsJson = OBJECT_MAPPER.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.send(requestAsJson);
        log.trace("Subscription payload sent at: {}", timestamp);
    }

    private BigInteger generateRandomReqId() {
        return RandomUtils.nextBigInteger(KRAKEN_REQ_ID_MAX_LIMIT);
    }
}
