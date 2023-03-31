//package com.kraken.api.javawrapper.websocket.model.message;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.kraken.api.javawrapper.websocket.model.message.embedded.BookLevelEmbeddedObject;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;
//import lombok.experimental.SuperBuilder;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//@Data
//@EqualsAndHashCode(callSuper = true)
//@SuperBuilder
//@AllArgsConstructor
//@NoArgsConstructor
//public class BookSnapshotMessage extends BookMessage {
//    private List<BookLevelEmbeddedObject> askLevels;
//    private List<BookLevelEmbeddedObject> bidLevels;
//
//    public static BookSnapshotMessage fromJsonNodeList(List<JsonNode> jsonNodeList, Integer depth) {
//        List<BookLevelEmbeddedObject> askLevels = new ArrayList<>();
//        Iterator<JsonNode> askLevelsIterator = jsonNodeList.get(1).get("as").elements();
//        while (askLevelsIterator.hasNext()) {
//            JsonNode askLevelNode = askLevelsIterator.next();
//            askLevels.add(BookLevelEmbeddedObject.builder()
//                .price(new BigDecimal(askLevelNode.get(0).asText()))
//                .volume(new BigDecimal(askLevelNode.get(1).asText()))
//                .timestamp(askLevelNode.get(2).asText())
//                .build());
//        }
//        List<BookLevelEmbeddedObject> bidLevels = new ArrayList<>();
//        Iterator<JsonNode> bidLevelsIterator = jsonNodeList.get(1).get("bs").elements();
//        while (bidLevelsIterator.hasNext()) {
//            JsonNode bidLevelNode = bidLevelsIterator.next();
//            bidLevels.add(BookLevelEmbeddedObject.builder()
//                .price(new BigDecimal(bidLevelNode.get(0).asText()))
//                .volume(new BigDecimal(bidLevelNode.get(1).asText()))
//                .timestamp(bidLevelNode.get(2).asText())
//                .build());
//        }
//        return BookSnapshotMessage.builder()
//            .isSnapshot(true)
//            .channelId(jsonNodeList.get(0).asInt())
//            .askLevels(askLevels)
//            .bidLevels(bidLevels)
//            .depth(depth)
//            .pair(jsonNodeList.get(jsonNodeList.size() - 1).asText())
//            .build();
//    }
//}
