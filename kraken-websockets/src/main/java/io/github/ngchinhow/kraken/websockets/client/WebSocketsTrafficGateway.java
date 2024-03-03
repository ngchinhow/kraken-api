package io.github.ngchinhow.kraken.websockets.client;

import io.github.ngchinhow.kraken.websockets.dto.request.RequestIdentifier;
import io.github.ngchinhow.kraken.websockets.dto.request.SubscriptionRequestIdentifier;
import io.github.ngchinhow.kraken.websockets.model.message.AbstractPublicationMessage;
import io.github.ngchinhow.kraken.websockets.model.message.status.StatusMessage;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractInteractionResponse;
import io.github.ngchinhow.kraken.websockets.model.method.AbstractResponse;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelResult;
import io.github.ngchinhow.kraken.websockets.model.method.echo.PongResponse;
import io.github.ngchinhow.kraken.websockets.model.method.subscription.SubscribeResponse;
import io.github.ngchinhow.kraken.websockets.model.method.unsubscription.UnsubscribeResponse;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import lombok.Getter;

import java.util.*;

@Getter
public class WebSocketsTrafficGateway {

    public final ReplaySubject<StatusMessage> statusMessages = ReplaySubject.create();

    /**
     * This is a {@link Map} of {@link RequestIdentifier} to {@link PublishSubject}
     * of subclasses of {@link AbstractResponse}.
     */
    private final Map<RequestIdentifier, ReplaySubject<?>> requestsToResponsesMap = new LinkedHashMap<>();

    /**
     * This is a {@link Map} of {@link SubscriptionRequestIdentifier} to
     * {@link PublishSubject} of subclasses of {@link AbstractPublicationMessage}.
     */
    private final Map<SubscriptionRequestIdentifier, ReplaySubject<?>> subscriptionsToPublicationsMap = new HashMap<>();

    public void responseAnnounce(StatusMessage statusMessage) {
        statusMessages.onNext(statusMessage);
    }

    public <T extends RequestIdentifier, U extends AbstractResponse> Single<U> registerRequest(T requestIdentifier) {
        final var replaySubject = ReplaySubject.<U>create(1);
        requestsToResponsesMap.put(requestIdentifier, replaySubject);
        return replaySubject.firstOrError();
    }

    @SuppressWarnings("unchecked")
    public <P extends AbstractPublicationMessage> ReplaySubject<P> subscribePublication(SubscriptionRequestIdentifier subscriptionRequestIdentifier) {
        return (ReplaySubject<P>) subscriptionsToPublicationsMap
            .computeIfAbsent(subscriptionRequestIdentifier, k -> ReplaySubject.<P>create());
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractResponse> void removeErrorRequest(T response) {
        final var requestIdentifier = response.toRequestIdentifier();
        boolean isSubscriptionError = false;
        boolean isSubscriptionNotFoundResponse = false;

        final var mapEntryIterator = requestsToResponsesMap.entrySet().iterator();

        ReplaySubject<T> replaySubject = null;
        while (mapEntryIterator.hasNext()) {
            final var entry = mapEntryIterator.next();
            final var mapRequestIdentifier = entry.getKey();
            final var mapRequestIdentifierList = new ArrayList<RequestIdentifier>();
            mapRequestIdentifierList.add(mapRequestIdentifier);
            if (mapRequestIdentifier instanceof SubscriptionRequestIdentifier mapSubscriptionRequestIdentifier) {
                isSubscriptionError = true;
                mapRequestIdentifierList.add(mapSubscriptionRequestIdentifier.buildForNoSymbolFound());
                mapRequestIdentifierList.add(mapSubscriptionRequestIdentifier.buildForAlreadySubscribed());
                mapRequestIdentifierList.add(mapSubscriptionRequestIdentifier.buildForNoSubscriptionFound());
            }

            boolean containsKey = false;
            for (int i = 0; i < mapRequestIdentifierList.size(); i++) {
                final var element = mapRequestIdentifierList.get(i);
                containsKey = element.equals(requestIdentifier) &&
                              element.getTimestamp().isBefore(requestIdentifier.getTimestamp());

                if (containsKey) {
                    if (isSubscriptionError && i == mapRequestIdentifierList.size() - 1)
                        // last index is for no subscription found request identifier
                        isSubscriptionNotFoundResponse = true;

                    break;
                }
            }

            if (containsKey) {
                replaySubject = (ReplaySubject<T>) entry.getValue();
                mapEntryIterator.remove();
                break;
            }
        }

        if (replaySubject != null) {
            if (isSubscriptionNotFoundResponse) {
                final var unsubscribeResponse = ((SubscribeResponse<?, ?>) response).toUnsubscribeResponse();
                ((ReplaySubject<UnsubscribeResponse<?>>) replaySubject).onNext(unsubscribeResponse);
            } else {
                replaySubject.onNext(response);
            }
            replaySubject.onComplete();
        } else {
            throw new NoSuchElementException("Request identifier could not be found for response " + response);
        }
    }

    /**
     * Ping/Pong messages are not considered to be interactions, i.e. they do not have a "result" nested object.
     * Duplicated method for this edge case.
     *
     * @param response pong response coming from a ping request
     */
    @SuppressWarnings("unchecked")
    public void forwardPongResponse(PongResponse response) {
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
    public <U extends AbstractInteractionResponse<?>> void forwardInteractionResponse(U response) {
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
    void unsubscribeRequest(UnsubscribeResponse<R> unsubscribeResponse) {
        SubscriptionRequestIdentifier channelRequestIdentifier = unsubscribeResponse.toSubscribeRequestIdentifier();
        ReplaySubject<P> abstractPublishMessageSubject = (ReplaySubject<P>) subscriptionsToPublicationsMap
            .remove(channelRequestIdentifier);
        abstractPublishMessageSubject.onComplete();
    }
}
