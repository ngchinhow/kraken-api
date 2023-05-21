package io.github.ngchinhow.kraken.websocket.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.websocket.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractMessage;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websocket.model.message.HeartbeatMessage;
import io.github.ngchinhow.kraken.websocket.model.message.status.StatusMessage;
import io.github.ngchinhow.kraken.websocket.model.method.*;
import io.github.ngchinhow.kraken.websocket.model.method.echo.PingRequest;
import io.github.ngchinhow.kraken.websocket.model.method.echo.PongResponse;
import io.github.ngchinhow.kraken.websocket.model.method.subscription.SubscribeRequest;
import io.github.ngchinhow.kraken.websocket.model.method.subscription.SubscribeResponse;
import io.github.ngchinhow.kraken.websocket.model.method.unsubscription.UnsubscribeRequest;
import io.github.ngchinhow.kraken.websocket.model.method.unsubscription.UnsubscribeResponse;
import io.github.ngchinhow.kraken.websocket.utils.RandomUtils;
import io.github.ngchinhow.kraken.websocket.utils.WebSocketTrafficGateway;
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

import static io.github.ngchinhow.kraken.properties.KrakenProperties.KRAKEN_REQ_ID_MAX_LIMIT;
import static io.github.ngchinhow.kraken.properties.KrakenProperties.WEBSOCKET_OBJECT_MAPPER;

/**
 * Basal class handling logic against Kraken's WebSockets V2 API
 *
 * @see <a href="https://docs.kraken.com/websockets-v2">Kraken WebSockets V2 API</a>
 */
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
            abstractResponse = WEBSOCKET_OBJECT_MAPPER.readValue(s, AbstractResponse.class);
            isResponse = true;
        } catch (JsonProcessingException e) {
            log.trace("Received message is not an AbstractResponse. {}", e.getLocalizedMessage());
        }
        if (isResponse) {
            if (abstractResponse instanceof AbstractInteractionResponse<?> interactionResponse &&
                !interactionResponse.getSuccess()) {
                log.error(interactionResponse.getError());
                webSocketTrafficGateway.removeErrorRequest(abstractResponse);
            } else {
                webSocketTrafficGateway.responseReply(abstractResponse);
                if (abstractResponse instanceof UnsubscribeResponse<?, ?> unsubscribeResponse)
                    webSocketTrafficGateway.unsubscribeRequest(unsubscribeResponse);
            }
        } else {
            try {
                abstractMessage = WEBSOCKET_OBJECT_MAPPER.readValue(s, AbstractMessage.class);
                isMessage = true;
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Received message is of unknown type. " + ex.getMessage());
            }
        }
        if (isMessage) {
            if (abstractMessage instanceof HeartbeatMessage heartbeatMessage) {
                //TODO: Implement default (or passed as parameter) keep-alive max time. Use frequency of
                // HeartbeatMessages to extend max time.
            } else if (abstractMessage instanceof StatusMessage statusMessage) {
                webSocketTrafficGateway.responseAnnounce(statusMessage);
            } else if (abstractMessage instanceof AbstractPublicationMessage publicationMessage) {
                webSocketTrafficGateway.publishMessage(publicationMessage);
            } else {
                log.error("Mismatch error: object passes through object mapper but is not of a known type. " +
                    "Received class is {}", abstractMessage.getClass());
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

    public Single<PongResponse> ping() {
        return ping(new PingRequest());
    }

    public Single<PongResponse> ping(PingRequest pingRequest) {
        if (Objects.isNull(pingRequest.getRequestId())) pingRequest.setRequestId(this.generateRandomReqId());
        ZonedDateTime serverTime = marketDataClient.getServerTime().getIsoTime();
        // PingRequests do not have any symbols involved, so there is only one entry - the one associated with null
        RequestIdentifier requestIdentifier = pingRequest.toRequestIdentifier(serverTime);
        ReplaySubject<PongResponse> pongMessageReplaySubject = ReplaySubject.create();
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
     *     If the same SubscribeRequest is sent twice or more to the same connection/client, the error received within
     *     the SubscribeResponse will be passed to the user. The PublishSubject originally created in the first request
     *     will also be returned with the SubscribeResponse.
     *   </li>
     * </ol>
     * <p>
     *
     * @param subscribeRequest The subscription payload from the user
     * @return List of Single of SubscribeResponses
     * @see <a href="https://reactivex.io/documentation/subject.html">ReactiveX Subject</a>
     * @see <a href="https://docs.kraken.com/websockets-v2/#subscribe">Kraken Subscribe</a>
     */
    public <R extends AbstractResult, P extends AbstractPublicationMessage, T extends AbstractParameter>
    List<Single<SubscribeResponse<R, P>>> subscribe(SubscribeRequest<T> subscribeRequest) {
        if (Objects.isNull(subscribeRequest.getRequestId())) subscribeRequest.setRequestId(this.generateRandomReqId());
        ZonedDateTime serverTime = marketDataClient.getServerTime().getIsoTime();
        List<RequestIdentifier> requestIdentifiers = subscribeRequest.toRequestIdentifiers(serverTime);
        List<Single<SubscribeResponse<R, P>>> list = new ArrayList<>();
        for (RequestIdentifier requestIdentifier : requestIdentifiers) {
            ReplaySubject<SubscribeResponse<R, P>> subscribeResponseReplaySubject = ReplaySubject.create(1);
            webSocketTrafficGateway.registerRequest(requestIdentifier, subscribeResponseReplaySubject);
            // Publication messages do not have req_id or timestamp information, so the RequestIdentifier cannot have
            // these field as keys in the map
            RequestIdentifier publicationRequestIdentifier = requestIdentifier.duplicate();
            PublishSubject<P> publicationMessagePublishSubject = webSocketTrafficGateway
                .subscribePublication(publicationRequestIdentifier);
            Single<SubscribeResponse<R, P>> subscribeResponseSingle = webSocketTrafficGateway
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

    /**
     * Unsubscription to a private or public channel via the Unsubscription payload.
     * <p>
     * While it is no longer necessary to the user, each UnsubscribeResponse will still contain the PublishSubject for
     * the corresponding AbstractPublicationMessage.
     *
     * @param unsubscribeRequest The unsubscription payload from the user
     * @return List of Single of UnsubscribeResponses
     * @see <a href="https://reactivex.io/documentation/subject.html">ReactiveX Subject</a>
     * @see <a href="https://docs.kraken.com/websockets-v2/#unsubscribe">Kraken Unsubscribe</a>
     */
    public <R extends AbstractResult, P extends AbstractPublicationMessage, T extends AbstractParameter>
    List<Single<UnsubscribeResponse<R, P>>> unsubscribe(UnsubscribeRequest<T> unsubscribeRequest) {
        if (Objects.isNull(unsubscribeRequest.getRequestId()))
            unsubscribeRequest.setRequestId(this.generateRandomReqId());
        ZonedDateTime serverTime = marketDataClient.getServerTime().getIsoTime();
        List<RequestIdentifier> unsubscribeRequestIdentifiers = unsubscribeRequest.toRequestIdentifiers(serverTime);
        List<Single<UnsubscribeResponse<R, P>>> list = new ArrayList<>();
        for (RequestIdentifier requestIdentifier : unsubscribeRequestIdentifiers) {
            ReplaySubject<UnsubscribeResponse<R, P>> unsubscribeResponseSubject = ReplaySubject.create();
            webSocketTrafficGateway.registerRequest(requestIdentifier, unsubscribeResponseSubject);
            RequestIdentifier publicationRequestIdentifier = requestIdentifier.duplicate();
            PublishSubject<P> publicationMessagePublishSubject = webSocketTrafficGateway
                .subscribePublication(publicationRequestIdentifier);
            Single<UnsubscribeResponse<R, P>> unsubscribeResponseSingle = webSocketTrafficGateway
                .retrieveResponse(requestIdentifier);
            unsubscribeResponseSingle = unsubscribeResponseSingle.map(e -> {
                e.setPublicationMessagePublishSubject(publicationMessagePublishSubject);
                return e;
            });
            list.add(unsubscribeResponseSingle);
        }
        this.sendPayload(unsubscribeRequest, serverTime);
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

    private <T extends AbstractRequest> void sendPayload(T request, ZonedDateTime timestamp) {
        String requestAsJson;
        try {
            requestAsJson = WEBSOCKET_OBJECT_MAPPER.writeValueAsString(request);
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
