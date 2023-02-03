package org.trading.krakenapi.websocket.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trading.krakenapi.websocket.dto.publication.PublicationMessage;
import org.trading.krakenapi.websocket.dto.publication.PublicationMessageFactory;
import org.trading.krakenapi.websocket.enums.WebSocketEnumerations;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PublicationMessageObjectDeserializer extends StdDeserializer<PublicationMessage> {
    private PublicationMessageFactory factory;

    protected PublicationMessageObjectDeserializer() {
        super(PublicationMessage.class);
    }

    @Autowired
    public void setFactory(PublicationMessageFactory factory) {
        this.factory = factory;
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
            return factory.fromJsonNodeList(objectList, channel);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
