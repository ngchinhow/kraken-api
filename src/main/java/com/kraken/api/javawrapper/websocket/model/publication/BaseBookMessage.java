package com.kraken.api.javawrapper.websocket.model.publication;

import com.fasterxml.jackson.databind.JsonNode;
import com.kraken.api.javawrapper.websocket.dto.request.SubscribeRequestIdentifier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Objects;

import static com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations.CHANNEL.BOOK;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class BaseBookMessage extends AbstractPublicationMessage {
    private boolean isSnapshot;
    private Integer depth;

    {
        this.setChannelName(BOOK);
    }

    @SuppressWarnings("unused")
    public static BaseBookMessage fromJsonNodeList(List<JsonNode> jsonNodeList) {
        JsonNode askLevels = jsonNodeList.get(1).get("as");
        String channel = jsonNodeList.get(jsonNodeList.size() - 2).asText();
        Integer depth = Integer.parseInt(channel.split("-")[1]);
        if (Objects.nonNull(askLevels))
            return BookSnapshotMessage.fromJsonNodeList(jsonNodeList, depth);
        else
            return BookUpdateMessage.fromJsonNodeList(jsonNodeList, depth);
    }

    @Override
    public SubscribeRequestIdentifier toSubscribeRequestIdentifier() {
        return super.toSubscribeRequestIdentifier().toBuilder()
            .depth(this.depth)
            .build();
    }
}
