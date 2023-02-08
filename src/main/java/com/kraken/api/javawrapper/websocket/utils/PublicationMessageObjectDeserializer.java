package com.kraken.api.javawrapper.websocket.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import com.kraken.api.javawrapper.websocket.dto.publication.PublicationMessage;
import com.kraken.api.javawrapper.websocket.dto.publication.PublicationMessageFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PublicationMessageObjectDeserializer extends StdDeserializer<PublicationMessage> {

    public PublicationMessageObjectDeserializer() {
        super(PublicationMessage.class);
    }

    @Override
    public PublicationMessage deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode array = p.getCodec().readTree(p);
        List<JsonNode> objectList = new ArrayList<>();
        Iterator<JsonNode> objectIterator = array.elements();
        int objectIndex = 0;
        WebSocketEnumerations.CHANNEL channel = null;

        while (objectIterator.hasNext()) {
            JsonNode node = objectIterator.next();
            if (objectIndex == 1 || objectIndex == 2)
                channel = WebSocketEnumerations.CHANNEL.getEChannel(node.asText().split("-")[0]);
            objectList.add(node);
            objectIndex++;
        }

        assert channel != null;
        try {
            return PublicationMessageFactory.fromJsonNodeList(objectList, channel);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
