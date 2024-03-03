package io.github.ngchinhow.kraken.websockets.model.method.order.cancelallafter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelAllOrdersAfterResult {
    private ZonedDateTime currentTime;
    private ZonedDateTime triggerTime;
}
