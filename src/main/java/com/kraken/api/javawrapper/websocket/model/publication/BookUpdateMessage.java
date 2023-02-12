package com.kraken.api.javawrapper.websocket.model.publication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.kraken.api.javawrapper.websocket.model.publication.embedded.BookLevelContainerObject;
import com.kraken.api.javawrapper.websocket.model.publication.embedded.BookLevelEmbeddedObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateMessage extends BaseBookMessage {
    private BookLevelContainerObject askUpdates;
    private BookLevelContainerObject bidUpdates;
    private static final Map<String, Method> UPDATE_KEY_TO_FIELD_MAP = new HashMap<>() {{
        try {
            put("a", BookUpdateMessageBuilderImpl.class.getMethod("askUpdates", BookLevelContainerObject.class));
            put("b", BookUpdateMessageBuilderImpl.class.getMethod("bidUpdates", BookLevelContainerObject.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }};

    public static BookUpdateMessage fromJsonNodeList(List<JsonNode> jsonNodeList, int depth) {
        BookUpdateMessageBuilder<?, ?> messageBuilder = new BookUpdateMessage().toBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode contentArray = objectMapper.createArrayNode();
        contentArray.add(jsonNodeList.get(1));
        if (jsonNodeList.get(2).isObject()) contentArray.add(jsonNodeList.get(2));
        Iterator<JsonNode> contentArrayIterator = contentArray.elements();
        while (contentArrayIterator.hasNext()) {
            BookLevelContainerObject.BookLevelContainerObjectBuilder containerBuilder = BookLevelContainerObject.builder();
            JsonNode contentNode = contentArrayIterator.next();
            JsonNode levels;
            for (Map.Entry<String, Method> entry : UPDATE_KEY_TO_FIELD_MAP.entrySet()) {
                JsonNode checksumNode;
                if ((checksumNode = contentNode.get("c")) != null)
                    containerBuilder.checksum(checksumNode.asInt());
                if ((levels = contentNode.get(entry.getKey())) != null) {
                    List<BookLevelEmbeddedObject> levelsList = new ArrayList<>();
                    Iterator<JsonNode> levelsIterator = levels.elements();
                    while (levelsIterator.hasNext()) {
                        JsonNode levelArray = levelsIterator.next();
                        levelsList.add(
                            BookLevelEmbeddedObject.builder()
                                .price(new BigDecimal(levelArray.get(0).asText()))
                                .volume(new BigDecimal(levelArray.get(1).asText()))
                                .timestamp(levelArray.get(2).asText())
                                .updateType(levelArray.get(3))
                                .build()
                        );
                    }
                    containerBuilder.levels(levelsList);
                    try {
                        entry.getValue().invoke(messageBuilder, containerBuilder.build());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return messageBuilder
            .channelId(jsonNodeList.get(0).asInt())
            .depth(depth)
            .pair(jsonNodeList.get(jsonNodeList.size() - 1).asText())
            .build();
    }
}
