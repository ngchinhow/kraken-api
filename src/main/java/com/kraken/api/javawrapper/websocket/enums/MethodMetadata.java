package com.kraken.api.javawrapper.websocket.enums;

public abstract class MethodMetadata {
    public static final String METHOD = "method";

    public static class MethodType {
        public static final String PING = "ping";
        public static final String PONG = "pong";
        public static final String SUBSCRIBE = "subscribe";
        public static final String UNSUBSCRIBE = "unsubscribe";
        public static final String ADD_ORDER = "add_order";
        public static final String BATCH_ADD = "batch_add";
        public static final String BATCH_CANCEL = "batch_cancel";
        public static final String CANCEL_ALL = "cancel_all";
        public static final String CANCEL_ALL_ORDERS_AFTER = "cancel_all_orders_after";
        public static final String CANCEL_ORDER = "cancel_order";
        public static final String EDIT_ORDER = "edit_order";
    }
}
