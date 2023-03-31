package com.kraken.api.javawrapper.websocket.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public abstract class ChannelMetadata {
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

    public enum ChangeType {
        SNAPSHOT("snapshot"),
        UPDATE("update");

        private final String changeType;

        ChangeType(String changeType) {
            this.changeType = changeType;
        }

        @JsonValue
        public String getChangeType() {
            return this.changeType;
        }
    }
}
