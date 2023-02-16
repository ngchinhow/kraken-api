package com.kraken.api.javawrapper.websocket.utils;

import com.kraken.api.javawrapper.websocket.dto.RequestIdentifier;
import com.kraken.api.javawrapper.websocket.dto.SubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.dto.UnsubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.event.AbstractInteractiveMessage;
import com.kraken.api.javawrapper.websocket.model.event.SystemStatusMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.IResponseMessage;
import com.kraken.api.javawrapper.websocket.model.publication.AbstractPublicationMessage;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Data
@NoArgsConstructor
public class WebSocketTrafficGateway {

    public final PublishSubject<SystemStatusMessage> systemStatusMessages = PublishSubject.create();
    // This is strictly a Map of RequestIdentifiers to PublishSubjects of subclasses of IResponseMessage
    // But since Java is fussy about types this is the easiest way
    private static final Map<RequestIdentifier, Object> REQUESTS_TO_PUBLISH_SUBJECT_MAP = new HashMap<>();
    // This is strictly a Map of SubscribeRequestIdentifiers to PublishSubjects of subclasses of
    // AbstractPublicationMessages. But since Java is fussy about types, this is the easiest way.
    private static final Map<SubscribeRequestIdentifier, Object> SUBSCRIPTION_TO_PUBLICATION_QUEUE = new HashMap<>();

    public <T extends RequestIdentifier, U extends IResponseMessage> void put(
        T requestIdentifier,
        PublishSubject<U> responseMessagePublishSubject
    ) {
        REQUESTS_TO_PUBLISH_SUBJECT_MAP.put(requestIdentifier, responseMessagePublishSubject);
    }

    @SuppressWarnings("unchecked")
    public <T extends IResponseMessage> void offer(T responseMessage) {
        ((PublishSubject<T>) REQUESTS_TO_PUBLISH_SUBJECT_MAP.get(responseMessage.toRequestIdentifier()))
            .onNext(responseMessage);
    }

    public <T extends RequestIdentifier> void remove(T requestIdentifier) {
        REQUESTS_TO_PUBLISH_SUBJECT_MAP.remove(requestIdentifier);
    }

    public <T extends AbstractPublicationMessage> ConcurrentLinkedQueue<T> subscribe(SubscribeRequestIdentifier requestIdentifier) {
        ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();
        SUBSCRIPTION_TO_PUBLICATION_QUEUE.put(requestIdentifier, queue);
        return queue;
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractPublicationMessage> void publish(T publicationMessage) {
        ((ConcurrentLinkedQueue<T>) SUBSCRIPTION_TO_PUBLICATION_QUEUE.get(publicationMessage.toSubscribeRequestIdentifier()))
            .offer(publicationMessage);
    }

    public void unsubscribe(UnsubscribeRequestIdentifier requestIdentifier) {
        SUBSCRIPTION_TO_PUBLICATION_QUEUE.remove(requestIdentifier.toSubscribeRequestIdentifier());
    }
}
