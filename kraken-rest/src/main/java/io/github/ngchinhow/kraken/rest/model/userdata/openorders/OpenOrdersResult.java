package io.github.ngchinhow.kraken.rest.model.userdata.openorders;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.github.ngchinhow.kraken.rest.model.ResultInterface;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter(value = AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OpenOrdersResult implements ResultInterface {
    private Open open;

    @Getter
    @Setter(value = AccessLevel.PACKAGE)
    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Open {
        private Map<String, OpenOrder> openOrders = new HashMap<>();

        @JsonAnySetter
        public void addOpenOrder(String transactionId, OpenOrder openOrder) {
            openOrders.put(transactionId, openOrder);
        }
    }
}
