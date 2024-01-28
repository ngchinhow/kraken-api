package io.github.ngchinhow.kraken.websocket.model.message.book;

import io.github.ngchinhow.kraken.websocket.dto.request.SubscriptionRequestIdentifier;
import io.github.ngchinhow.kraken.websocket.enums.ChannelMetadata;
import io.github.ngchinhow.kraken.websocket.model.message.AbstractPublicationMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class BookMessage extends AbstractPublicationMessage {
    private List<Book> data;

    {
        this.setChannel(ChannelMetadata.ChannelType.BOOK);
    }

    @Override
    public SubscriptionRequestIdentifier toRequestIdentifier() {
        return super.toRequestIdentifier().toBuilder()
            .symbol(data.get(0).getSymbol())
            .build();
    }
}
