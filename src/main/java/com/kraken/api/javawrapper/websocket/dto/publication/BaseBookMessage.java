package com.kraken.api.javawrapper.websocket.dto.publication;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Objects;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.CHANNEL.BOOK;

@Getter
@Setter
@SuperBuilder
@Jacksonized
@AllArgsConstructor
public class BaseBookMessage extends PublicationMessage {
    private int depth;

    public BaseBookMessage() {
        this.setChannelName(BOOK);
    }

    @SuppressWarnings("unused")
    public static BaseBookMessage fromJsonNodeList(List<JsonNode> jsonNodeList) {
        JsonNode askLevels = jsonNodeList.get(1).get("as");
        String channel = jsonNodeList.get(jsonNodeList.size() - 2).asText();
        int depth = Integer.parseInt(channel.split("-")[1]);
        if (Objects.nonNull(askLevels))
            return BookSnapshotMessage.fromJsonNodeList(jsonNodeList, depth);
        else
            return BookUpdateMessage.fromJsonNodeList(jsonNodeList, depth);
    }
}
