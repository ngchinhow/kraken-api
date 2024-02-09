package io.github.ngchinhow.kraken.websockets.enums;

import io.github.ngchinhow.kraken.common.enumerations.BaseKrakenEnum;

public final class ChannelMetadata {

    private ChannelMetadata() throws IllegalAccessException {
        throw new IllegalAccessException("Static constants");
    }

    public static final String CHANNEL = "channel";

    public static class ChannelType {
        public static final String BOOK = "book";
        public static final String EXECUTIONS = "executions";
        public static final String HEARTBEAT = "heartbeat";
        public static final String INSTRUMENT = "instrument";
        public static final String TICKER = "ticker";
        public static final String OHLC = "ohlc";
        public static final String TRADE = "trade";
        public static final String STATUS = "status";
    }

    public enum ChangeType implements BaseKrakenEnum {
        SNAPSHOT,
        UPDATE
    }
}
