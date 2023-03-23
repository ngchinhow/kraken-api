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
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class WebSocketTrafficGateway {

    public final PublishSubject<SystemStatusMessage> systemStatusMessages = PublishSubject.create();

    // This is strictly a Map of RequestIdentifiers to PublishSubjects of subclasses of IResponseMessage
    // But since Java is fussy about types this is the easiest way
    private final Map<RequestIdentifier, Object> requestsToResponsesMap = new HashMap<>();

    // This is strictly a Map of SubscribeRequestIdentifiers to PublishSubjects of subclasses of
    // AbstractPublicationMessages. But since Java is fussy about types, this is the easiest way.
    private final Map<SubscribeRequestIdentifier, Object> subscriptionsToPublicationsMap = new HashMap<>();

    public <T extends RequestIdentifier, U extends IResponseMessage> void registerRequest(
        T requestIdentifier,
        ReplaySubject<U> responseMessageReplaySubject
    ) {
        requestsToResponsesMap.put(requestIdentifier, responseMessageReplaySubject);
    }

    public void responseAnnounce(SystemStatusMessage systemStatusMessage) {
        systemStatusMessages.onNext(systemStatusMessage);
    }

    @SuppressWarnings("unchecked")
    public <U extends IResponseMessage> void responseReply(U responseMessage) {
        RequestIdentifier requestIdentifier = responseMessage.toRequestIdentifier();
        ReplaySubject<U> replaySubject = (ReplaySubject<U>) requestsToResponsesMap.get(requestIdentifier);
        replaySubject.onNext(responseMessage);
    }

    @SuppressWarnings("unchecked")
    public <U extends IResponseMessage> void responseReplyAndRemove(U responseMessage) {
        this.responseReply(responseMessage);
        // For 1-to-1 messages, registration is removed when response is received.
        // Currently, this only applies for Ping/Pong Messages
        ReplaySubject<U> responseMessageSubject = (ReplaySubject<U>) requestsToResponsesMap
            .remove(responseMessage.toRequestIdentifier());
        responseMessageSubject.onComplete();
    }

    public boolean isRequestSubscribed(SubscribeRequestIdentifier requestIdentifier) {
        return subscriptionsToPublicationsMap.containsKey(requestIdentifier);
    }

    @SuppressWarnings("unchecked")
    public <U extends IResponseMessage> Single<U> retrieveResponse(RequestIdentifier requestIdentifier) {
        return ((ReplaySubject<U>) requestsToResponsesMap.get(requestIdentifier)).firstOrError();
    }

    public PublishSubject<AbstractPublicationMessage> subscribeRequest(SubscribeRequestIdentifier requestIdentifier) {
        PublishSubject<AbstractPublicationMessage> publicationMessageReplaySubject = PublishSubject.create();
        subscriptionsToPublicationsMap.put(requestIdentifier, publicationMessageReplaySubject);
        return publicationMessageReplaySubject;
    }

    @SuppressWarnings("unchecked")
    public PublishSubject<AbstractPublicationMessage> getSubscriptionSubject(SubscribeRequestIdentifier requestIdentifier) {
        return (PublishSubject<AbstractPublicationMessage>) subscriptionsToPublicationsMap.get(requestIdentifier);
    }

    @SuppressWarnings("unchecked")
    public <P extends AbstractPublicationMessage> void publishMessage(P publicationMessage) {
        SubscribeRequestIdentifier subscribeRequestIdentifier = publicationMessage.toSubscribeRequestIdentifier();
        ((PublishSubject<P>) subscriptionsToPublicationsMap.get(subscribeRequestIdentifier)).onNext(publicationMessage);
    }

    @SuppressWarnings("unchecked")
    public <P extends AbstractPublicationMessage> void unsubscribeRequest(UnsubscribeRequestIdentifier requestIdentifier) {
        PublishSubject<P> abstractPublishMessageSubject = (PublishSubject<P>) subscriptionsToPublicationsMap
            .remove(requestIdentifier.toSubscribeRequestIdentifier());
        abstractPublishMessageSubject.onComplete();
    }
}
