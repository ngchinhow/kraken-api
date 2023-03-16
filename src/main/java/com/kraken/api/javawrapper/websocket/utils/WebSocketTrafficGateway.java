package com.kraken.api.javawrapper.websocket.utils;

import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import com.kraken.api.javawrapper.websocket.dto.request.SubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.dto.request.UnsubscribeRequestIdentifier;
import com.kraken.api.javawrapper.websocket.model.event.SystemStatusMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.IResponseMessage;
import com.kraken.api.javawrapper.websocket.model.publication.AbstractPublicationMessage;
import io.reactivex.rxjava3.core.Single;
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
        ReplaySubject<U> responseMessageReplaySubject
    ) {
        requestsToResponseMap.put(requestIdentifier, responseMessageReplaySubject);
    }

    @SuppressWarnings("unchecked")
    public <T extends IResponseMessage> void reply(T responseMessage) {
        RequestIdentifier requestIdentifier = responseMessage.toRequestIdentifier();
        ReplaySubject<T> replaySubject = (ReplaySubject<T>) requestsToResponseMap.get(requestIdentifier);
        replaySubject.onNext(responseMessage);
    }

    public <T extends IResponseMessage> void replyAndRemove(T responseMessage) {
        this.reply(responseMessage);
        // For 1-to-1 messages, registration is removed when response is received.
        // Currently, this only applies for Ping/Pong Messages
        requestsToResponseMap.remove(responseMessage.toRequestIdentifier());
    }

    public boolean isSubscribed(SubscribeRequestIdentifier requestIdentifier) {
        return subscriptionsToPublicationMap.containsKey(requestIdentifier);
    }

    @SuppressWarnings("unchecked")
    public <T extends IResponseMessage> Single<T> retrieve(SubscribeRequestIdentifier requestIdentifier) {
        return ((ReplaySubject<T>) requestsToResponseMap.get(requestIdentifier)).firstOrError();
    }

    public ReplaySubject<AbstractPublicationMessage> subscribe(SubscribeRequestIdentifier requestIdentifier) {
        ReplaySubject<AbstractPublicationMessage> publicationMessageReplaySubject = ReplaySubject.createWithSize(PUBLICATION_BUFFER_SIZE);
        subscriptionsToPublicationMap.put(requestIdentifier, publicationMessageReplaySubject);
        return publicationMessageReplaySubject;
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractPublicationMessage> void publish(T publicationMessage) {
        SubscribeRequestIdentifier subscribeRequestIdentifier = publicationMessage.toSubscribeRequestIdentifier();
        ((ReplaySubject<T>) subscriptionsToPublicationMap.get(subscribeRequestIdentifier)).onNext(publicationMessage);
    }

    public void unsubscribe(UnsubscribeRequestIdentifier requestIdentifier) {
        subscriptionsToPublicationMap.remove(requestIdentifier.toSubscribeRequestIdentifier());
    }
}
