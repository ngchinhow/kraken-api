package com.kraken.api.javawrapper.websocket.utils;

import com.kraken.api.javawrapper.websocket.model.event.AbstractEventMessage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedQueueUtils {

    public static <T extends AbstractEventMessage> ConcurrentLinkedQueue<T> create(Class<T> ignoredTClass) {
        return new ConcurrentLinkedQueue<>();
    }
}
