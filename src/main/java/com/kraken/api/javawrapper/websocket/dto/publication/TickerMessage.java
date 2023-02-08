package com.kraken.api.javawrapper.websocket.dto.publication;

import com.fasterxml.jackson.databind.JsonNode;
import com.kraken.api.javawrapper.websocket.enums.WebSocketEnumerations;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class TickerMessage extends PublicationMessage {
    private Price ask;
    private Price bid;
    private Price close;
    private Value volume;
    private Value vwap;
    private Value trades;
    private Value low;
    private Value high;
    private Value open;

    public TickerMessage() {
        this.setChannelName(WebSocketEnumerations.CHANNEL.TICKER);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Price {
        private BigDecimal price;
        private int wholeLotVolume;
        private BigDecimal lotVolume;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Value {
        private BigDecimal today;
        private BigDecimal last24Hours;
    }

    public static TickerMessage fromJsonNodeList(List<JsonNode> jsonNodeList, WebSocketEnumerations.CHANNEL channel) {
        return new TickerMessage().toBuilder()
            .channelId(jsonNodeList.get(0).asInt())
            .channelName(channel)
            .pair(jsonNodeList.get(3).asText())
            .ask(Price.builder()
                .price(new BigDecimal(jsonNodeList.get(1).get("a").get(0).asText()))
                .wholeLotVolume(jsonNodeList.get(1).get("a").get(1).asInt())
                .lotVolume(new BigDecimal(jsonNodeList.get(1).get("a").get(2).asText()))
                .build())
            .bid(Price.builder()
                .price(new BigDecimal(jsonNodeList.get(1).get("b").get(0).asText()))
                .wholeLotVolume(jsonNodeList.get(1).get("b").get(1).asInt())
                .lotVolume(new BigDecimal(jsonNodeList.get(1).get("b").get(2).asText()))
                .build())
            .close(Price.builder()
                .price(new BigDecimal(jsonNodeList.get(1).get("c").get(0).asText()))
                .lotVolume(new BigDecimal(jsonNodeList.get(1).get("c").get(1).asText()))
                .build())
            .volume(Value.builder()
                .today(new BigDecimal(jsonNodeList.get(1).get("v").get(0).asText()))
                .last24Hours(new BigDecimal(jsonNodeList.get(1).get("v").get(1).asText()))
                .build())
            .vwap(Value.builder()
                .today(new BigDecimal(jsonNodeList.get(1).get("p").get(0).asText()))
                .last24Hours(new BigDecimal(jsonNodeList.get(1).get("p").get(1).asText()))
                .build())
            .trades(Value.builder()
                .today(new BigDecimal(jsonNodeList.get(1).get("t").get(0).asText()))
                .last24Hours(new BigDecimal(jsonNodeList.get(1).get("t").get(1).asText()))
                .build())
            .low(Value.builder()
                .today(new BigDecimal(jsonNodeList.get(1).get("l").get(0).asText()))
                .last24Hours(new BigDecimal(jsonNodeList.get(1).get("l").get(1).asText()))
                .build())
            .high(Value.builder()
                .today(new BigDecimal(jsonNodeList.get(1).get("h").get(0).asText()))
                .last24Hours(new BigDecimal(jsonNodeList.get(1).get("h").get(1).asText()))
                .build())
            .open(Value.builder()
                .today(new BigDecimal(jsonNodeList.get(1).get("o").get(0).asText()))
                .last24Hours(new BigDecimal(jsonNodeList.get(1).get("o").get(1).asText()))
                .build())
            .build();
    }
}
