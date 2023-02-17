package com.kraken.api.javawrapper.websocket.dto.response;

import com.kraken.api.javawrapper.websocket.model.event.response.SubscriptionStatusMessage;
import com.kraken.api.javawrapper.websocket.model.publication.AbstractPublicationMessage;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscribedObject {
    // Contains the SubscriptionStatusMessage indicating a successful (or failed) subscription.
    private Single<SubscriptionStatusMessage> subscriptionStatusMessage;
    private ReplaySubject<AbstractPublicationMessage> publicationMessageReplaySubject;
}
