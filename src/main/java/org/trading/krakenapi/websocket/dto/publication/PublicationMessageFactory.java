package org.trading.krakenapi.websocket.dto.publication;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.trading.krakenapi.websocket.enums.WebSocketEnumerations;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class PublicationMessageFactory {
    private static final String DESERIALIZATION_METHOD_NAME = "fromJsonNodeList";

    public PublicationMessage fromJsonNodeList(List<JsonNode> jsonNodeList, WebSocketEnumerations.CHANNEL channel) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (PublicationMessage) channel.getPublicationMessageClass()
            .getMethod(DESERIALIZATION_METHOD_NAME, List.class, WebSocketEnumerations.CHANNEL.class)
            .invoke(null, jsonNodeList, channel);
    }
}
