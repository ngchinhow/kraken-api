package com.kraken.api.javawrapper.websocket.model.publication;

import com.fasterxml.jackson.databind.JsonNode;
import com.kraken.api.javawrapper.websocket.model.publication.embedded.TradeEmbeddedObject;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class TradeMessage extends AbstractPublicationMessage {
    private List<TradeEmbeddedObject> trades;

    public TradeMessage() {
        this.setChannelName(WebSocketEnumerations.CHANNEL.TRADE);
    }

    @SuppressWarnings("unused")
    public static TradeMessage fromJsonNodeList(List<JsonNode> jsonNodeList) {
        List<TradeEmbeddedObject> trades = new ArrayList<>();
        Iterator<JsonNode> tradesIterator = jsonNodeList.get(1).elements();
        while (tradesIterator.hasNext()) {
            JsonNode trade = tradesIterator.next();
            trades.add(TradeEmbeddedObject.builder()
                .price(new BigDecimal(trade.get(0).asText()))
                .volume(new BigDecimal(trade.get(1).asText()))
                .time(trade.get(2).asText())
                .side(trade.get(3).asText())
                .orderType(trade.get(4).asText())
                .misc(trade.get(5).asText())
                .build());
        }
        return new TradeMessage().toBuilder()
            .channelId(jsonNodeList.get(0).asInt())
            .pair(jsonNodeList.get(2).asText())
            .trades(trades)
            .build();
    }
}
