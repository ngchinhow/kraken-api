package com.kraken.api.javawrapper.websocket.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import com.kraken.api.javawrapper.websocket.enums.ChannelMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class BookMessage extends AbstractPublicationMessage {

    private List<Book> data;

    {
        this.setChannel(ChannelMetadata.ChannelType.BOOK);
    }

    @Override
    public RequestIdentifier toSubscribeRequestIdentifier() {
        return null;
    }

    @Data
    public static class Book {
        private List<BookEntry> asks;
        private List<BookEntry> bids;
        private Long checksum;
        private String symbol;
        private ZonedDateTime timestamp;

        @Data
        public static class BookEntry {
            private BigDecimal price;
            @JsonProperty("qty")
            private BigDecimal quantity;
        }
    }
}
