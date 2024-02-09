package io.github.ngchinhow.kraken.websockets.model.message.book;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
public class Book {
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
}