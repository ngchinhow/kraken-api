package io.github.ngchinhow.kraken.websockets.model.method.channel.book;

import io.github.ngchinhow.kraken.websockets.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websockets.model.method.channel.AbstractChannelResult;
import lombok.*;

@ToString
@Getter
@Setter(value = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public final class BookResult extends AbstractChannelResult {
    private Integer depth;
    @NonNull
    private String symbol;

    {
        setChannel(ChannelMetadata.ChannelType.BOOK);
    }
}
