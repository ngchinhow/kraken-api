package com.kraken.api.javawrapper.websocket.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeUtils {

    public static LocalDateTime epochMicroToLocalDateTime(String epochInMicro) {
        String[] components = epochInMicro.split("\\.");
        return Instant
            .ofEpochSecond(Long.parseLong(components[0]), Long.parseLong(components[1]) * 1000L)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }
}
