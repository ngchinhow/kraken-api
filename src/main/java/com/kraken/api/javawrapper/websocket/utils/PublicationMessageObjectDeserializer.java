package com.kraken.api.javawrapper.websocket.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import com.kraken.api.javawrapper.websocket.model.publication.AbstractPublicationMessage;
import com.kraken.api.javawrapper.websocket.model.publication.PublicationMessageFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PublicationMessageObjectDeserializer extends StdDeserializer<AbstractPublicationMessage> {

    public PublicationMessageObjectDeserializer() {
        super(AbstractPublicationMessage.class);
    }

    @Override
    public AbstractPublicationMessage deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        List<JsonNode> objectList = new ArrayList<>();
        JsonNode array = p.getCodec().readTree(p);
        array.elements().forEachRemaining(objectList::add);
        int secondLastIndex = objectList.size() - 2;
        WebSocketEnumerations.CHANNEL channel = WebSocketEnumerations.CHANNEL.getEChannel(
            objectList.get(secondLastIndex).asText().split("-")[0]
        );

        try {
            return PublicationMessageFactory.fromJsonNodeList(objectList, channel);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
