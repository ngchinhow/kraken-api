package com.kraken.api.javawrapper.websocket.utils;

import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.message.AbstractPublicationMessage;
import com.kraken.api.javawrapper.websocket.model.message.StatusMessage;
import com.kraken.api.javawrapper.websocket.model.method.AbstractResponse;
import com.kraken.api.javawrapper.websocket.model.method.Interaction;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import lombok.Getter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class WebSocketTrafficGateway {

    public final PublishSubject<StatusMessage> statusMessages = PublishSubject.create();

    // This is strictly a Map of RequestIdentifiers to PublishSubjects of subclasses of AbstractResponse
    // But since Java is fussy about types this is the easiest way
    private final Map<RequestIdentifier, Object> requestsToResponsesMap = new LinkedHashMap<>();

    // This is strictly a Map of SubscribeRequestIdentifiers to PublishSubjects of subclasses of
    // AbstractPublicationMessages. But since Java is fussy about types, this is the easiest way.
    private final Map<RequestIdentifier, Object> subscriptionsToPublicationsMap = new HashMap<>();

    public void responseAnnounce(StatusMessage statusMessage) {
        statusMessages.onNext(statusMessage);
    }

    public <T extends RequestIdentifier, U extends AbstractResponse> void registerRequest(
        T requestIdentifier,
        ReplaySubject<U> responseMessageReplaySubject
    ) {
        requestsToResponsesMap.put(requestIdentifier, responseMessageReplaySubject);
    }

    public boolean isRequestSubscribed(RequestIdentifier requestIdentifier) {
        return subscriptionsToPublicationsMap.containsKey(requestIdentifier);
    }

    @SuppressWarnings("unchecked")
    public <U extends Interaction.AbstractInteractionResponse> Single<U> retrieveResponse(RequestIdentifier requestIdentifier) {
        return ((ReplaySubject<U>) requestsToResponsesMap.get(requestIdentifier)).firstOrError();
    }

    @SuppressWarnings("unchecked")
    public PublishSubject<AbstractPublicationMessage> subscribePublication(RequestIdentifier requestIdentifier) {
        PublishSubject<AbstractPublicationMessage> publicationMessageReplaySubject =
            (PublishSubject<AbstractPublicationMessage>) subscriptionsToPublicationsMap.getOrDefault(
                requestIdentifier,
                PublishSubject.create()
            );
        subscriptionsToPublicationsMap.putIfAbsent(requestIdentifier, publicationMessageReplaySubject);
        return publicationMessageReplaySubject;
    }

    @SuppressWarnings("unchecked")
    public PublishSubject<AbstractPublicationMessage> getSubscriptionSubject(RequestIdentifier requestIdentifier) {
        return (PublishSubject<AbstractPublicationMessage>) subscriptionsToPublicationsMap.get(requestIdentifier);
    }

    @SuppressWarnings("unchecked")
    public <U extends AbstractResponse> void removeErrorRequest(U response) {
        Iterator<Map.Entry<RequestIdentifier, Object>> mapIterator = requestsToResponsesMap.entrySet().iterator();
        // Insertion order is guaranteed. Assumed that response order is same as request order
        Map.Entry<RequestIdentifier, Object> nextMapEntry = mapIterator.next();
        RequestIdentifier requestEntry = nextMapEntry.getKey();
        assert requestEntry.getRequestId().equals(response.getRequestId());
        assert response.getTimeIn().isAfter(requestEntry.getTimestamp());
        ReplaySubject<U> responseSubject = (ReplaySubject<U>) nextMapEntry.getValue();
        responseSubject.onNext(response);
        responseSubject.onComplete();
        mapIterator.remove();
    }

    @SuppressWarnings("unchecked")
    public <U extends AbstractResponse> void responseReply(U response) {
        RequestIdentifier requestIdentifier = response.toRequestIdentifier();
        ReplaySubject<U> responseSubject = (ReplaySubject<U>) requestsToResponsesMap.remove(requestIdentifier);
        responseSubject.onNext(response);
        responseSubject.onComplete();
    }

    @SuppressWarnings("unchecked")
    public <P extends AbstractPublicationMessage> void publishMessage(P publicationMessage) {
        RequestIdentifier subscribeRequestIdentifier = publicationMessage.toRequestIdentifier();
        ((PublishSubject<P>) subscriptionsToPublicationsMap.get(subscribeRequestIdentifier)).onNext(publicationMessage);
    }

    @SuppressWarnings("unchecked")
    public <P extends AbstractPublicationMessage> void unsubscribeRequest(RequestIdentifier requestIdentifier) {
        PublishSubject<P> abstractPublishMessageSubject = (PublishSubject<P>) subscriptionsToPublicationsMap
            .remove(requestIdentifier);
        abstractPublishMessageSubject.onComplete();
    }
}
