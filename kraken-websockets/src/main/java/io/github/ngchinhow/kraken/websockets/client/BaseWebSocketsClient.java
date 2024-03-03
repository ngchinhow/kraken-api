package io.github.ngchinhow.kraken.websockets.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.websockets.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websockets.dto.request.SubscriptionRequestIdentifier;
import io.github.ngchinhow.kraken.websockets.model.message.AbstractMessage;
import io.github.ngchinhow.kraken.websockets.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websockets.model.message.HeartbeatMessage;
import io.github.ngchinhow.kraken.websockets.model.message.status.StatusMessage;
import io.github.ngchinhow.kraken.websockets.model.method.*;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelParameter;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelResult;
import io.github.ngchinhow.kraken.websockets.model.method.echo.PingRequest;
import io.github.ngchinhow.kraken.websockets.model.method.echo.PongResponse;
import io.github.ngchinhow.kraken.websockets.model.method.subscription.SubscribeRequest;
import io.github.ngchinhow.kraken.websockets.model.method.subscription.SubscribeResponse;
import io.github.ngchinhow.kraken.websockets.model.method.unsubscription.UnsubscribeRequest;
import io.github.ngchinhow.kraken.websockets.model.method.unsubscription.UnsubscribeResponse;
import io.github.ngchinhow.kraken.websockets.utils.RandomUtils;
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

import static io.github.ngchinhow.kraken.websockets.properties.WebSocketsProperties.OBJECT_MAPPER;
import static io.github.ngchinhow.kraken.websockets.properties.WebSocketsProperties.REQ_ID_MAX_LIMIT;

/**
 * Basal class handling logic against Kraken's WebSockets V2 API
 *
 * @see <a href="https://docs.kraken.com/websockets-v2">Kraken WebSockets V2 API</a>
 */
@Slf4j
@Getter
public abstract class BaseWebSocketsClient extends WebSocketClient {
    protected final WebSocketsTrafficGateway webSocketsTrafficGateway = new WebSocketsTrafficGateway();
    private final MarketDataClient marketDataClient;
    private final CompositeDisposable disposableBin = new CompositeDisposable();
    private LocalDateTime clientOpenTime;

    public BaseWebSocketsClient(final URI krakenWebSocketUrl, MarketDataClient marketDataClient) {
        super(krakenWebSocketUrl);
        this.marketDataClient = marketDataClient;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.trace("Connection opened with status: {}", serverHandshake.getHttpStatusMessage());
        this.clientOpenTime = LocalDateTime.now();
    }

    @Override
    public void onMessage(String s) {
        log.trace("Received message: {}", s);
        AbstractResponse abstractResponse = null;
        AbstractMessage abstractMessage;
        boolean isResponse = false;
        try {
            while (!isResponse) {
                try {
                    abstractResponse = OBJECT_MAPPER.readValue(s, AbstractResponse.class);
                    isResponse = true;
                } catch (UnrecognizedPropertyException e) {
                    // Currently, only known occurrence is when unsubscription is performed before any subscription happened.
                    // Response is returned with method of "subscribe" instead of "unsubscribe".
//                    s = s.replace(SUBSCRIBE, UNSUBSCRIBE);
                }
            }
        } catch (JsonProcessingException e) {
            log.trace("Received message is not an AbstractResponse. {}", e.getLocalizedMessage());
        }
        if (isResponse) {
            log.trace("Response class: {}", abstractResponse);

            if (abstractResponse instanceof PongResponse pongResponse) {
                webSocketsTrafficGateway.forwardPongResponse(pongResponse);
                return;
            }
            var abstractInteractionResponse = (AbstractInteractionResponse<?>) abstractResponse;

            if (!abstractInteractionResponse.getSuccess()) {
                // Handle errors
                log.error(abstractInteractionResponse.getError());
                webSocketsTrafficGateway.removeErrorRequest(abstractResponse);
                return;
            }

            var result = abstractInteractionResponse.getResult();
            if (result instanceof AbstractResult abstractResult && abstractResult.getWarnings() != null) {
                final var warnings = abstractResult.getWarnings();
                // Handle warning
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < warnings.size(); i++) {
                    stringBuilder.append(i + 1)
                                 .append(": ")
                                 .append(warnings.get(i))
                                 .append("\n");
                }
                log.warn(stringBuilder.toString());
            }
            webSocketsTrafficGateway.forwardInteractionResponse(abstractInteractionResponse);

            if (abstractInteractionResponse instanceof UnsubscribeResponse<?> unsubscribeResponse)
                webSocketsTrafficGateway.unsubscribeRequest(unsubscribeResponse);

            return;
        }
        try {
            abstractMessage = OBJECT_MAPPER.readValue(s, AbstractMessage.class);
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
            case StatusMessage statusMessage -> webSocketsTrafficGateway.responseAnnounce(statusMessage);
            case AbstractPublicationMessage publicationMessage ->
                webSocketsTrafficGateway.publishMessage(publicationMessage);
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
        return sendDirectRequest(pingRequest);
    }

    /**
     * Subscription to a public or private channel via the Subscribe payload.
     * <p>
     * Behaviour caveats:
     * <ol>
     *   <li>
     *     A {@link ReplaySubject} is used for the messages of the subscription. Because there will always be a delay
     *     between when this "subscribe" method is called and the "subscribe" method on the {@link Single} is called,
     *     this Subject ensures that no messages are missed out during this period.
     *   </li>
     *   <li>
     *     If the same {@link SubscribeRequest} is sent twice or more to the same connection/client, the error received
     *     within the {@link SubscribeResponse} will be passed to the user. The ReplaySubject originally created in the
     *     first request will also be returned with the SubscribeResponse.
     *   </li>
     * </ol>
     * <p>
     *
     * @param subscribeRequest The subscription payload from the user
     * @return List of Single of SubscribeResponses
     * @see <a href="https://reactivex.io/documentation/subject.html">ReactiveX Subject</a>
     * @see <a href="https://docs.kraken.com/websockets-v2/#subscribe">Kraken Subscribe</a>
     */
    public <R extends AbstractChannelResult, P extends AbstractPublicationMessage, T extends AbstractChannelParameter>
    List<Single<SubscribeResponse<R, P>>> subscribe(SubscribeRequest<T> subscribeRequest) {
        checkRequestId(subscribeRequest);

        checkPrivateParameter(subscribeRequest.getParams());

        var serverTime = getServerTime();
        var requestIdentifiers = subscribeRequest.toRequestIdentifiers(serverTime);
        var list = new ArrayList<Single<SubscribeResponse<R, P>>>();
        for (var requestIdentifier : requestIdentifiers) {
            var responseSingle = webSocketsTrafficGateway.<RequestIdentifier, SubscribeResponse<R, P>>registerRequest(requestIdentifier);
            final var publicationSubscriptionRequestIdentifier = ((SubscriptionRequestIdentifier) requestIdentifier).buildForPublicationMessages();
            final var publicationMessageReplaySubject = webSocketsTrafficGateway.<P>subscribePublication(publicationSubscriptionRequestIdentifier);
            responseSingle = responseSingle.map(e -> {
                e.setPublicationMessageReplaySubject(publicationMessageReplaySubject);
                return e;
            });
            list.add(responseSingle);
        }
        sendPayload(subscribeRequest, serverTime);
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
    public <R extends AbstractChannelResult, T extends AbstractChannelParameter>
    List<Single<UnsubscribeResponse<R>>> unsubscribe(UnsubscribeRequest<T> unsubscribeRequest) {
        return sendExpandingRequest(unsubscribeRequest);
    }

    private BigInteger generateRandomReqId() {
        return RandomUtils.nextBigInteger(REQ_ID_MAX_LIMIT);
    }

    /**
     * Add custom request ID if not present
     *
     * @param request Request to be sent to Kraken
     */
    protected void checkRequestId(AbstractRequest request) {
        if (request.getRequestId() == null)
            request.setRequestId(generateRandomReqId());
    }

    protected void checkPrivateParameter(ParameterInterface param) {
    }

    protected ZonedDateTime getServerTime() {
        return marketDataClient.getServerTime().getIsoTime();
    }

    protected <T extends AbstractRequest, U extends AbstractResponse> Single<U> sendDirectRequest(T request) {
        checkRequestId(request);

        if (request instanceof AbstractInteractionRequest<?> interactionRequest)
            checkPrivateParameter(interactionRequest.getParams());

        final var serverTime = getServerTime();
        final var requestIdentifier = request.toRequestIdentifier(serverTime);
        final var responseSingle = webSocketsTrafficGateway.<RequestIdentifier, U>registerRequest(requestIdentifier);
        sendPayload(request, serverTime);
        return responseSingle;
    }

    protected <T extends ParameterInterface, U extends AbstractInteractionRequest<T>, V extends AbstractResponse>
    List<Single<V>> sendExpandingRequest(U request) {
        checkRequestId(request);

        checkPrivateParameter(request.getParams());

        final var serverTime = getServerTime();
        final var requestIdentifiers = request.toRequestIdentifiers(serverTime);
        final var list = new ArrayList<Single<V>>();
        for (var requestIdentifier : requestIdentifiers) {
            final var responseSingle = webSocketsTrafficGateway.<RequestIdentifier, V>registerRequest(requestIdentifier);
            list.add(responseSingle);
        }
        sendPayload(request, serverTime);
        return list;
    }

    protected <T extends AbstractRequest> void sendPayload(T request, ZonedDateTime timestamp) {
        String requestAsJson;
        try {
            requestAsJson = OBJECT_MAPPER.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        send(requestAsJson);
        log.trace("WebSockets payload sent at: {}", timestamp);
    }
}
