package org.trading.krakenapi.websocket.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeUtils {

    public static LocalDateTime epochMicroToLocalDateTime(String epochInMicro) {
        String[] epochComponents = epochInMicro.split("\\.");
        return Instant
            .ofEpochSecond(
                Long.parseLong(epochComponents[0]),
                Long.parseLong(epochComponents[1]) * 1000L
            )
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }
}
