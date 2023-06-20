package io.github.ngchinhow.kraken.websocket.utils;

import io.github.ngchinhow.kraken.websocket.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websocket.model.message.status.StatusMessage;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractResponse;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractInteractionResponse;
import io.github.ngchinhow.kraken.websocket.model.method.AbstractResult;
import io.github.ngchinhow.kraken.websocket.model.method.unsubscription.UnsubscribeResponse;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import lombok.Getter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class WebSocketTrafficGateway {

    public final ReplaySubject<StatusMessage> statusMessages = ReplaySubject.create();

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

    @SuppressWarnings("unchecked")
    public <R extends AbstractResult, U extends AbstractInteractionResponse<R>> Single<U> retrieveResponse(RequestIdentifier requestIdentifier) {
        return ((ReplaySubject<U>) requestsToResponsesMap.get(requestIdentifier)).firstOrError();
    }

    @SuppressWarnings("unchecked")
    public <P extends AbstractPublicationMessage> ReplaySubject<P> subscribePublication(RequestIdentifier requestIdentifier) {
        ReplaySubject<P> publicationMessageReplaySubject =
            (ReplaySubject<P>) subscriptionsToPublicationsMap.getOrDefault(
                requestIdentifier,
                ReplaySubject.create()
            );
        subscriptionsToPublicationsMap.putIfAbsent(requestIdentifier, publicationMessageReplaySubject);
        return publicationMessageReplaySubject;
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
        ((ReplaySubject<P>) subscriptionsToPublicationsMap.get(subscribeRequestIdentifier)).onNext(publicationMessage);
    }

    @SuppressWarnings("unchecked")
    public <R extends AbstractResult, P extends AbstractPublicationMessage>
    void unsubscribeRequest(UnsubscribeResponse<R, P> unsubscribeResponse) {
        RequestIdentifier requestIdentifier = unsubscribeResponse.toRequestIdentifier();
        requestIdentifier.setRequestId(null);
        ReplaySubject<P> abstractPublishMessageSubject = (ReplaySubject<P>) subscriptionsToPublicationsMap
            .remove(requestIdentifier);
        abstractPublishMessageSubject.onComplete();
    }
}
