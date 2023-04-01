package com.kraken.api.javawrapper.websocket.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.api.javawrapper.websocket.dto.request.RequestIdentifier;
import com.kraken.api.javawrapper.websocket.enums.ChannelMetadata;
import lombok.*;
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
    public RequestIdentifier toRequestIdentifier() {
        return super.toRequestIdentifier().toBuilder()
            .symbol(data.get(0).symbol)
            .build();
    }

    @Data
    @NoArgsConstructor(force = true)
    public static class Book {
        @NonNull
        private List<BookEntry> asks;
        @NonNull
        private List<BookEntry> bids;
        @NonNull
        private Long checksum;
        @NonNull
        private String symbol;
        @NonNull
        private ZonedDateTime timestamp;

        @Data
        @NoArgsConstructor(force = true)
        public static class BookEntry {
            @NonNull
            private BigDecimal price;
            @JsonProperty(value = "qty", required = true)
            private BigDecimal quantity;
        }
    }
}
