package org.trading.krakenapi.websocket.dto.general;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseSubscriptionMessage extends GeneralMessage {
    private String reqid;
    private JsonNode pair;
    private SubscriptionEmbeddedObject subscription;

    public static abstract class BaseSubscriptionMessageBuilder<
        C extends GeneralMessage,
        B extends GeneralMessageBuilder<C, B>
    > extends GeneralMessageBuilder<C, B> {
        public B pair(List<String> pair) {
            ObjectMapper objectMapper = new ObjectMapper();
            this.pair = objectMapper.valueToTree(pair);
            return self();
        }
    }
}
