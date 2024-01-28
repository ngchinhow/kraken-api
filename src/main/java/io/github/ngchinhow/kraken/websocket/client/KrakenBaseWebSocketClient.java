package io.github.ngchinhow.kraken.websocket.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.websocket.dto.request.SubscriptionRequestIdentifier;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractResult;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractMessage;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websocket.model.message.HeartbeatMessage;
import io.github.ngchinhow.kraken.websocket.model.message.status.StatusMessage;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractInteractionResponse;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractRequest;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractResponse;
import io.github.ngchinhow.kraken.websocket.model.method.ParameterInterface;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelResult;
import io.github.ngchinhow.kraken.websocket.model.method.echo.PingRequest;
import io.github.ngchinhow.kraken.websocket.model.method.echo.PongResponse;
import io.github.ngchinhow.kraken.websocket.model.method.subscription.SubscribeRequest;
import io.github.ngchinhow.kraken.websocket.model.method.subscription.SubscribeResponse;
import io.github.ngchinhow.kraken.websocket.model.method.unsubscription.UnsubscribeRequest;
import io.github.ngchinhow.kraken.websocket.model.method.unsubscription.UnsubscribeResponse;
import io.github.ngchinhow.kraken.websocket.utils.RandomUtils;
import io.github.ngchinhow.kraken.websocket.utils.WebSocketTrafficGateway;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.math.BigInteger;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.ngchinhow.kraken.properties.KrakenProperties.KRAKEN_REQ_ID_MAX_LIMIT;
import static io.github.ngchinhow.kraken.properties.KrakenProperties.WEBSOCKET_OBJECT_MAPPER;
import static io.github.ngchinhow.kraken.websocket.enums.MethodMetadata.MethodType.SUBSCRIBE;
import static io.github.ngchinhow.kraken.websocket.enums.MethodMetadata.MethodType.UNSUBSCRIBE;

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
    private final CompositeDisposable disposableBin = new CompositeDisposable();
    private LocalDateTime clientOpenTime;

    public KrakenBaseWebSocketClient(final URI krakenWebSocketUrl, MarketDataClient marketDataClient) {
        super(krakenWebSocketUrl);
        this.marketDataClient = marketDataClient;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.trace("Connection opened with status: {}", serverHandshake.getHttpStatusMessage());
        this.clientOpenTime = LocalDateTime.now();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onMessage(String s) {
        log.trace("Received message: {}", s);
        AbstractResponse abstractResponse = null;
        AbstractMessage abstractMessage;
        boolean isResponse = false;
        try {
            while (!isResponse) {
                try {
                    abstractResponse = WEBSOCKET_OBJECT_MAPPER.readValue(s, AbstractResponse.class);
                    isResponse = true;
                } catch (UnrecognizedPropertyException e) {
                    // Currently, only known occurrence is when unsubscription is performed before any subscription happened.
                    // Response is returned with method of "subscribe" instead of "unsubscribe".
                    s = s.replace(SUBSCRIBE, UNSUBSCRIBE);
                }
            }
        } catch (JsonProcessingException e) {
            log.trace("Received message is not an AbstractResponse. {}", e.getLocalizedMessage());
        }
        if (isResponse) {
            log.trace("Response class: {}", abstractResponse);

            if (abstractResponse instanceof PongResponse pongResponse) {
                webSocketTrafficGateway.responseReplyPong(pongResponse);
                return;
            }
            var abstractInteractionResponse = (AbstractInteractionResponse<? extends AbstractResult>) abstractResponse;

            if (!abstractInteractionResponse.getSuccess()) {
                // Handle errors
                log.error(abstractInteractionResponse.getError());
                webSocketTrafficGateway.removeErrorRequest(abstractResponse);
                return;
            }

            var warnings = abstractInteractionResponse.getResult().getWarnings();
            if (warnings != null) {
                // Handle warnings
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < warnings.size(); i++) {
                    stringBuilder.append(i + 1)
                                 .append(": ")
                                 .append(warnings.get(i))
                                 .append("\n");
                }
                log.warn(stringBuilder.toString());
            }
            webSocketTrafficGateway.responseReplyInteraction(abstractInteractionResponse);

            if (abstractInteractionResponse instanceof UnsubscribeResponse<?, ?> unsubscribeResponse)
                webSocketTrafficGateway.unsubscribeRequest(unsubscribeResponse);

            return;
        }
        try {
            abstractMessage = WEBSOCKET_OBJECT_MAPPER.readValue(s, AbstractMessage.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Received message is of unknown type. " + ex.getMessage());
        }
        log.trace("Message class: {}", abstractMessage);

        if (abstractMessage == null) {
            log.error("Unexpected null message. Stopping subscription");
            throw new NullPointerException("Unexpected null message");
        }

        switch (abstractMessage) {
            case HeartbeatMessage ignored -> {
                var heartBeatReceivedTime = LocalDateTime.now();
                if (clientOpenTime.plusSeconds(50).isBefore(heartBeatReceivedTime) &&
                    clientOpenTime.plusSeconds(60).isAfter(heartBeatReceivedTime)) {
                    var pongDisposable = this.ping()
                                             .subscribe(p -> {
                                                 log.trace(
                                                     "Connection extended successfully. Updating connection start time to {}",
                                                     heartBeatReceivedTime);
                                                 clientOpenTime = heartBeatReceivedTime;
                                             }, e -> {
                                                 log.error(
                                                     "Error faced when extending connection with message: {}. Failing now.",
                                                     e.getMessage());
                                                 throw e;
                                             });
                    disposableBin.add(pongDisposable);
                }
            }
            case StatusMessage statusMessage -> webSocketTrafficGateway.responseAnnounce(statusMessage);
            case AbstractPublicationMessage publicationMessage ->
                webSocketTrafficGateway.publishMessage(publicationMessage);
            default -> log.error("Mismatch error: object passes through object mapper but is not of a known type. " +
                                 "Received class is {}", abstractMessage.getClass());
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.trace("Connection closed. i: {}, s: {}, b: {}", i, s, b);
        disposableBin.dispose();
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
        var channelRequestIdentifier = pingRequest.toRequestIdentifier(serverTime);
        ReplaySubject<PongResponse> pongMessageReplaySubject = ReplaySubject.create();
        webSocketTrafficGateway.registerRequest(channelRequestIdentifier, pongMessageReplaySubject);
        this.sendPayload(pingRequest, serverTime);
        return pongMessageReplaySubject.firstOrError();
    }

    /**
     * Subscription to a public or private channel via the Subscribe payload.
     * <p>
     * Behaviour caveats:
     * <ol>
     *   <li>
     *     A RxJava ReplaySubject is used for the messages of the subscription. Because there will always be a delay
     *     between when this "subscribe" method is called and the "subscribe" method on the Single is called, this
     *     Subject ensures that no messages are missed out during this period.
     *   </li>
     *   <li>
     *     If the same SubscribeRequest is sent twice or more to the same connection/client, the error received within
     *     the SubscribeResponse will be passed to the user. The ReplaySubject originally created in the first request
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
    public <R extends AbstractChannelResult, P extends AbstractPublicationMessage, T extends ParameterInterface>
    List<Single<SubscribeResponse<R, P>>> subscribe(SubscribeRequest<T> subscribeRequest) {
        if (subscribeRequest.getRequestId() == null)
            subscribeRequest.setRequestId(this.generateRandomReqId());

        var serverTime = marketDataClient.getServerTime().getIsoTime();
        var requestIdentifiers = subscribeRequest.toRequestIdentifiers(serverTime);
        var list = new ArrayList<Single<SubscribeResponse<R, P>>>();
        for (var requestIdentifier : requestIdentifiers) {
            var subscriptionRequestIdentifier = (SubscriptionRequestIdentifier) requestIdentifier;
            var subscribeResponseReplaySubject = ReplaySubject.<SubscribeResponse<R, P>>create(1);
            webSocketTrafficGateway.registerRequest(subscriptionRequestIdentifier, subscribeResponseReplaySubject);
            // Publication messages do not have req_id or timestamp information, so the RequestIdentifier cannot have
            // these field as keys in the map
            var publicationSubscriptionRequestIdentifier = subscriptionRequestIdentifier.duplicate();
            ReplaySubject<P> publicationMessageReplaySubject = webSocketTrafficGateway
                .subscribePublication(publicationSubscriptionRequestIdentifier);
            Single<SubscribeResponse<R, P>> subscribeResponseSingle = webSocketTrafficGateway.retrieveResponse(
                requestIdentifier);
            subscribeResponseSingle = subscribeResponseSingle.map(e -> {
                e.setPublicationMessageReplaySubject(publicationMessageReplaySubject);
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
    public <R extends AbstractChannelResult, P extends AbstractPublicationMessage, T extends ParameterInterface>
    List<Single<UnsubscribeResponse<R, P>>> unsubscribe(UnsubscribeRequest<T> unsubscribeRequest) {
        if (unsubscribeRequest.getRequestId() == null)
            unsubscribeRequest.setRequestId(this.generateRandomReqId());

        ZonedDateTime serverTime = marketDataClient.getServerTime().getIsoTime();
        var requestIdentifiers = unsubscribeRequest.toRequestIdentifiers(serverTime);
        var list = new ArrayList<Single<UnsubscribeResponse<R, P>>>();
        for (var requestIdentifier : requestIdentifiers) {
            var unsubscribeRequestIdentifier = (SubscriptionRequestIdentifier) requestIdentifier;
            var unsubscribeResponseSubject = ReplaySubject.<UnsubscribeResponse<R, P>>create();
            webSocketTrafficGateway.registerRequest(requestIdentifier, unsubscribeResponseSubject);
            SubscriptionRequestIdentifier publicationChannelRequestIdentifier = unsubscribeRequestIdentifier.duplicate();
            ReplaySubject<P> publicationMessageReplaySubject = webSocketTrafficGateway
                .subscribePublication(publicationChannelRequestIdentifier);
            Single<UnsubscribeResponse<R, P>> unsubscribeResponseSingle = webSocketTrafficGateway
                .retrieveResponse(requestIdentifier);
            unsubscribeResponseSingle = unsubscribeResponseSingle.map(e -> {
                e.setPublicationMessageReplaySubject(publicationMessageReplaySubject);
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
