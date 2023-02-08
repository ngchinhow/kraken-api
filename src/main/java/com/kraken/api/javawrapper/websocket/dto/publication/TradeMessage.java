package com.kraken.api.javawrapper.websocket.dto.publication;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class TradeMessage extends PublicationMessage {
    private List<TradeEmbeddedObject> trades;

    public TradeMessage() {
        this.setChannelName(WebSocketEnumerations.CHANNEL.TRADE);
    }

    public static TradeMessage fromJsonNodeList(List<JsonNode> jsonNodeList, WebSocketEnumerations.CHANNEL channel) {
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
            .channelName(channel)
            .pair(jsonNodeList.get(2).asText())
            .trades(trades)
            .build();
    }
}
