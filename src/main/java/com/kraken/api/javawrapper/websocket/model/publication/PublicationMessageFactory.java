package com.kraken.api.javawrapper.websocket.model.publication;

import com.fasterxml.jackson.databind.JsonNode;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class PublicationMessageFactory {
    private static final String DESERIALIZATION_METHOD_NAME = "fromJsonNodeList";

    public static AbstractPublicationMessage fromJsonNodeList(List<JsonNode> jsonNodeList, WebSocketEnumerations.CHANNEL channel) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (AbstractPublicationMessage) channel.getPublicationMessageClass()
            .getMethod(DESERIALIZATION_METHOD_NAME, List.class)
            .invoke(null, jsonNodeList);
    }
}
