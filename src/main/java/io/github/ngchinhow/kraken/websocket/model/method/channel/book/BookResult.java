package io.github.ngchinhow.kraken.websocket.model.method.channel.book;

import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.method.channel.AbstractChannelResult;
import lombok.*;

@ToString
@Getter
@Setter(value = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
public final class BookResult extends AbstractChannelResult {
    private final Integer depth;
    @NonNull
    private final String symbol;

    {
        this.setChannel(ChannelMetadata.ChannelType.BOOK);
    }
}
