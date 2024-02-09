package io.github.ngchinhow.kraken.websockets.client;

import io.github.ngchinhow.kraken.websockets.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websockets.dto.request.SubscriptionRequestIdentifier;
import io.github.ngchinhow.kraken.websockets.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websockets.model.message.status.StatusMessage;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionResponse;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractResponse;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelResult;
import io.github.ngchinhow.kraken.websockets.model.method.echo.PongResponse;
import io.github.ngchinhow.kraken.websockets.model.method.unsubscription.UnsubscribeResponse;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class WebSocketsTrafficGateway {

    public final ReplaySubject<StatusMessage> statusMessages = ReplaySubject.create();

    /**
     * This is a {@link Map} of {@link RequestIdentifier} to {@link io.reactivex.rxjava3.subjects.PublishSubject}
     * of subclasses of {@link AbstractResponse}.
     */
    @SuppressWarnings("rawtypes")
    private final Map<RequestIdentifier, ReplaySubject> requestsToResponsesMap = new LinkedHashMap<>();

    /**
     * This is a {@link Map} of {@link SubscriptionRequestIdentifier} to
     * {@link io.reactivex.rxjava3.subjects.PublishSubject} of subclasses of {@link AbstractPublicationMessage}.
     */
    @SuppressWarnings("rawtypes")
    private final Map<SubscriptionRequestIdentifier, ReplaySubject> subscriptionsToPublicationsMap = new HashMap<>();

    public void responseAnnounce(StatusMessage statusMessage) {
        statusMessages.onNext(statusMessage);
    }

    public <T extends RequestIdentifier, U extends AbstractResponse> void registerRequest(
        T requestIdentifier,
        ReplaySubject<U> responseMessageReplaySubject) {
        requestsToResponsesMap.put(requestIdentifier, responseMessageReplaySubject);
    }

    @SuppressWarnings("unchecked")
    public <R extends AbstractChannelResult, U extends AbstractInteractionResponse<R>> Single<U> retrieveResponse(
        RequestIdentifier requestIdentifier) {
        return ((ReplaySubject<U>) requestsToResponsesMap.get(requestIdentifier)).firstOrError();
    }

    @SuppressWarnings("unchecked")
    public <P extends AbstractPublicationMessage> ReplaySubject<P> subscribePublication(SubscriptionRequestIdentifier subscriptionRequestIdentifier) {
        return (ReplaySubject<P>) subscriptionsToPublicationsMap
            .computeIfAbsent(subscriptionRequestIdentifier, k -> ReplaySubject.<P>create());
    }

    @SuppressWarnings("unchecked")
    public <U extends AbstractResponse> void removeErrorRequest(U response) {
        var mapIterator = requestsToResponsesMap.entrySet()
                                                .iterator();
        // Insertion order is guaranteed. Assumed that response order is same as request order
        var nextMapEntry = mapIterator.next();
        var requestEntry = nextMapEntry.getKey();
        assert requestEntry.getRequestId().equals(response.getRequestId());
        assert response.getTimeIn().isAfter(requestEntry.getTimestamp());
        ReplaySubject<U> responseSubject = (ReplaySubject<U>) nextMapEntry.getValue();
        responseSubject.onNext(response);
        responseSubject.onComplete();
        mapIterator.remove();
    }

    /**
     * Ping/Pong messages are not considered to be interactions, i.e. they do not have a "result" nested object.
     * Duplicated method for this edge case.
     *
     * @param response pong response coming from a ping request
     */
    @SuppressWarnings("unchecked")
    public void responseReplyPong(PongResponse response) {
        var channelRequestIdentifier = response.toRequestIdentifier();
        var responseSubject = (ReplaySubject<PongResponse>) requestsToResponsesMap.remove(channelRequestIdentifier);
        responseSubject.onNext(response);
        responseSubject.onComplete();
    }

    /**
     * Any message that has a "result" nested object. Logically no different from how a PongResponse is treated.
     *
     * @param response Channel or Order response
     * @param <U>      Any type that extends AbstractInteractionResponse. Only Channels and Orders
     */
    @SuppressWarnings("unchecked")
    public <U extends AbstractInteractionResponse<?>> void responseReplyInteraction(U response) {
        var requestIdentifier = response.toRequestIdentifier();
        ReplaySubject<U> responseSubject = (ReplaySubject<U>) requestsToResponsesMap.remove(requestIdentifier);
        responseSubject.onNext(response);
        responseSubject.onComplete();
    }

    @SuppressWarnings("unchecked")
    public <P extends AbstractPublicationMessage> void publishMessage(P publicationMessage) {
        SubscriptionRequestIdentifier subscribeChannelRequestIdentifier = publicationMessage.toRequestIdentifier();
        ((ReplaySubject<P>) subscriptionsToPublicationsMap.get(subscribeChannelRequestIdentifier)).onNext(
            publicationMessage);
    }

    @SuppressWarnings("unchecked")
    public <R extends AbstractChannelResult, P extends AbstractPublicationMessage>
    void unsubscribeRequest(UnsubscribeResponse<R, P> unsubscribeResponse) {
        SubscriptionRequestIdentifier channelRequestIdentifier = unsubscribeResponse.toRequestIdentifier();
        channelRequestIdentifier.setRequestId(null);
        ReplaySubject<P> abstractPublishMessageSubject = (ReplaySubject<P>) subscriptionsToPublicationsMap
            .remove(channelRequestIdentifier);
        abstractPublishMessageSubject.onComplete();
    }
}
