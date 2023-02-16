package com.kraken.api.javawrapper.websocket.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kraken.api.javawrapper.threading.DaemonThreadFactory;
import com.kraken.api.javawrapper.websocket.model.event.AbstractEventMessage;
import com.kraken.api.javawrapper.websocket.model.event.SystemStatusMessage;
import com.kraken.api.javawrapper.websocket.model.event.embedded.SubscriptionEmbeddedObject;
import com.kraken.api.javawrapper.websocket.model.event.request.PingMessage;
import com.kraken.api.javawrapper.websocket.model.event.request.SubscribeMessage;
import com.kraken.api.javawrapper.websocket.model.event.request.UnsubscribeMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.IResponseMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.PongMessage;
import com.kraken.api.javawrapper.websocket.model.event.response.SubscriptionStatusMessage;
import com.kraken.api.javawrapper.websocket.model.publication.AbstractPublicationMessage;
import com.kraken.api.javawrapper.websocket.utils.PublicationMessageObjectDeserializer;
import com.kraken.api.javawrapper.websocket.utils.RandomUtils;
import com.kraken.api.javawrapper.websocket.utils.WebSocketTrafficGateway;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.math.BigInteger;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static com.kraken.api.javawrapper.properties.KrakenProperties.KRAKEN_REQ_ID_MAX_LIMIT;

@Slf4j
public class KrakenWebSocketClient extends WebSocketClient {
    private final ExecutorService executorService = Executors.newCachedThreadPool(new DaemonThreadFactory());
    private final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(
        JsonInclude.Include.NON_NULL
    );

    private final WebSocketTrafficGateway webSocketTrafficGateway = new WebSocketTrafficGateway();

    private final List<String> pairs;
    private final SubscriptionEmbeddedObject subscriptionEmbeddedObject;

    public KrakenWebSocketClient(URI uri, List<String> pairs, SubscriptionEmbeddedObject subscription) {
        super(uri);
        this.pairs = pairs;
        this.subscriptionEmbeddedObject = subscription;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
//        Map<Class<? extends AbstractEventMessage>, ConcurrentLinkedQueue<? extends AbstractEventMessage>> hashMap =
//            Arrays.stream(WebSocketEnumerations.EVENT.class.getDeclaredClasses()).map(c -> {
//                try {
//                    //noinspection unchecked
//                    return (Class<? extends AbstractEventMessage>) c.getField("").get(null);
//                } catch (IllegalAccessException | NoSuchFieldException e) {
//                    throw new RuntimeException(e);
//                }
//            }).collect(Collectors.toMap(Function.identity(), ConcurrentLinkedQueueUtils::create));
//        ConcurrentLinkedQueue<SubscribeMessage> t = (ConcurrentLinkedQueue<SubscribeMessage>) hashMap.get(SubscribeMessage.class);
        SubscribeMessage.SubscribeMessageBuilder<?, ?> subscribeMessageBuilder = SubscribeMessage.builder()
            .subscription(subscriptionEmbeddedObject);
        if (!pairs.isEmpty()) subscribeMessageBuilder.pair(pairs);
        PingMessage pingMessage = PingMessage.builder()
            .reqId(BigInteger.valueOf(1234))
            .build();
        String subscribeAsJson;
        String pingAsJson;
        try {
            subscribeAsJson = objectMapper.writeValueAsString(subscribeMessageBuilder.build());
            pingAsJson = objectMapper.writeValueAsString(pingMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("Subscription payload: {}", subscribeAsJson);
//        this.send(subscribeAsJson);
//        this.send(pingAsJson);
        try {
            subscribeAsJson = objectMapper.writeValueAsString(subscribeMessageBuilder
                .reqId(new BigInteger("11111111111111111111111111111111111111111111111"))
                .subscription(subscriptionEmbeddedObject.toBuilder().depth(100).build())
                .build()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("Subscription payload: {}", subscribeAsJson);
//        this.send(subscribeAsJson);
//        UnsubscribeMessage unsubscribeMessage = UnsubscribeMessage.builder()
//            .subscription(subscriptionEmbeddedObject)
//            .pair(pairs)
//            .build();
//        String unsubscribeAsJson;
//        try {
//            unsubscribeAsJson = objectMapper.writeValueAsString(unsubscribeMessage);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        log.info("Unsubscription payload: {}", unsubscribeAsJson);
//        this.send(unsubscribeAsJson);
    }

    @Override
    public void onMessage(String s) {
        log.info("Received message: {}", s);
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(AbstractPublicationMessage.class, new PublicationMessageObjectDeserializer());
        objectMapper.registerModule(simpleModule);
        AbstractEventMessage abstractEventMessage = null;
        AbstractPublicationMessage publicationMessage = null;
        boolean isEventMessage = false;
        try {
            abstractEventMessage = objectMapper.readValue(s, AbstractEventMessage.class);
            isEventMessage = true;
        } catch (JsonProcessingException e) {
            log.trace("Received message is not an AbstractEventMessage. {}", e.getLocalizedMessage());
        }
        if (isEventMessage) {
            if (abstractEventMessage instanceof IResponseMessage responseMessage) {
                webSocketTrafficGateway.offer(responseMessage);
            } else if (abstractEventMessage instanceof SystemStatusMessage systemStatusMessage) {
                webSocketTrafficGateway.getSystemStatusMessages().onNext(systemStatusMessage);
            }
        } else {
            try {
                publicationMessage = objectMapper.readValue(s, AbstractPublicationMessage.class);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Received message is of unknown type. " + ex.getMessage());
            }
        }
        if (Objects.nonNull(abstractEventMessage))
            log.info("Event message class: {}", abstractEventMessage);
        if (Objects.nonNull(publicationMessage))
            log.info("Publication message class: {}", publicationMessage);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.trace("Connection closed. i: {}, s: {}, b: {}", i, s, b);
    }

    @Override
    public void onError(Exception e) {
        log.error("Unexpected error during websocket operation: {}", e.getMessage());
        throw new RuntimeException(e);
    }

    public Single<PongMessage> ping() {
        return ping(new PingMessage());
    }

    public Single<PongMessage> ping(PingMessage pingMessage) {
        if (Objects.isNull(pingMessage.getReqId())) pingMessage.setReqId(this.generateRandomReqId());
        AtomicReference<PongMessage> pongMessage = new AtomicReference<>();
        PublishSubject<PongMessage> pongMessagePublishSubject = PublishSubject.create();
        webSocketTrafficGateway.put(pingMessage.toRequestIdentifier(), pongMessagePublishSubject);
        String pingAsJson;
        try {
            pingAsJson = objectMapper.writeValueAsString(pingMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.send(pingAsJson);
        return pongMessagePublishSubject.firstOrError();
//        return CompletableFuture.supplyAsync(
//            () -> {
//                if (Objects.isNull(pingMessage.getReqId())) pingMessage.setReqId(this.generateRandomReqId());
//                ConcurrentLinkedQueue<PongMessage> pongMessageQueue = new ConcurrentLinkedQueue<>();
//
//
//                //noinspection StatementWithEmptyBody,LoopConditionNotUpdatedInsideLoop
//                while (pongMessageQueue.isEmpty()) { }
//                PongMessage pongMessage = pongMessageQueue.poll();
//                webSocketTrafficGateway.remove(pongMessage.toRequestIdentifier());
//                return pongMessage;
//            },
//            executorService
//        );
    }

    public SubscriptionStatusMessage subscribe(SubscribeMessage subscribeMessage) {

        return null;
    }

    public SubscriptionStatusMessage unsubscribe(UnsubscribeMessage unsubscribeMessage) {

        return null;
    }

    public void addOrder() {

    }

    public void editOrder() {

    }

    public void cancelOrder() {

    }

    public void cancelAllOrders() {

    }

    public void cancelAllOrdersAfter() {

    }

    private BigInteger generateRandomReqId() {
        return RandomUtils.nextBigInteger(KRAKEN_REQ_ID_MAX_LIMIT);
    }
}
