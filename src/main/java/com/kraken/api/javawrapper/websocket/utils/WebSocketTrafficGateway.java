package com.kraken.api.javawrapper.websocket.utils;

import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import com.kraken.api.javawrapper.websocket.dto.request.SubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.dto.request.UnsubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.event.SystemStatusMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.IResponseMessage;
import com.kraken.api.javawrapper.websocket.model.publication.AbstractPublicationMessage;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class WebSocketTrafficGateway {

    public final PublishSubject<SystemStatusMessage> systemStatusMessages = PublishSubject.create();

    // This is strictly a Map of RequestIdentifiers to PublishSubjects of subclasses of IResponseMessage
    // But since Java is fussy about types this is the easiest way
    private final Map<RequestIdentifier, Object> requestsToResponseMap = new HashMap<>();

    // This is strictly a Map of SubscribeRequestIdentifiers to PublishSubjects of subclasses of
    // AbstractPublicationMessages. But since Java is fussy about types, this is the easiest way.
    private final Map<SubscribeRequestIdentifier, Object> subscriptionsToPublicationMap = new HashMap<>();
    private final int PUBLICATION_BUFFER_SIZE = 100;

    public <T extends RequestIdentifier, U extends IResponseMessage> void register(
        T requestIdentifier,
        PublishSubject<U> responseMessagePublishSubject
    ) {
        requestsToResponseMap.put(requestIdentifier, responseMessagePublishSubject);
    }

    @SuppressWarnings("unchecked")
    public <T extends IResponseMessage> void reply(T responseMessage) {
        RequestIdentifier requestIdentifier = responseMessage.toRequestIdentifier();
        // For 1-to-1 messages, registration is removed when response is received
        PublishSubject<T> publishSubject = (PublishSubject<T>) requestsToResponseMap.remove(requestIdentifier);
        publishSubject.onNext(responseMessage);
    }

    public boolean isSubscribed(SubscribeRequestIdentifier requestIdentifier) {
        return subscriptionsToPublicationMap.containsKey(requestIdentifier);
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractPublicationMessage> ReplaySubject<T> retrieve(SubscribeRequestIdentifier requestIdentifier) {
        return (ReplaySubject<T>) subscriptionsToPublicationMap.get(requestIdentifier);
    }

    public <T extends AbstractPublicationMessage> ReplaySubject<T> subscribe(SubscribeRequestIdentifier requestIdentifier) {
        ReplaySubject<T> replaySubject = ReplaySubject.createWithSize(PUBLICATION_BUFFER_SIZE);
        subscriptionsToPublicationMap.put(requestIdentifier, replaySubject);
        return replaySubject;
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractPublicationMessage> void publish(T publicationMessage) {
        ((ReplaySubject<T>) subscriptionsToPublicationMap.get(publicationMessage.toSubscribeRequestIdentifier()))
            .onNext(publicationMessage);
    }

    public void unsubscribe(UnsubscribeRequestIdentifier requestIdentifier) {
        subscriptionsToPublicationMap.remove(requestIdentifier.toSubscribeRequestIdentifier());
    }
}
