package com.kraken.api.javawrapper.websocket.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.kraken.api.javawrapper.websocket.model.publication.*;

import java.util.HashMap;
import java.util.Map;

public class WebSocketEnumerations {
        public static final String EVENT = "event";

    public static class EVENT_TYPE {
        public static final String PING = "ping";
        public static final String PONG = "pong";
        public static final String HEARTBEAT = "heartbeat";
        public static final String SYSTEM_STATUS = "systemStatus";
        public static final String SUBSCRIBE = "subscribe";
        public static final String UNSUBSCRIBE = "unsubscribe";
        public static final String SUBSCRIPTION_STATUS = "subscriptionStatus";
        public static final String ADD_ODER = "addOrder";
        public static final String ADD_ORDER_STATUS = "addOrderStatus";
        public static final String EDIT_ORDER = "editOrder";
        public static final String EDIT_ORDER_STATUS = "editOrderStatus";
        public static final String CANCEL_ORDER = "cancelOrder";
        public static final String CANCEL_ORDER_STATUS = "cancelOrderStatus";
        public static final String CANCEL_ALL = "cancelAll";
        public static final String CANCEL_ALL_STATUS = "cancelAllStatus";
        public static final String CANCEL_ALL_ORDERS_AFTER = "cancelAllOrdersAfter";
        public static final String CANCEL_ALL_ORDERS_AFTER_STATUS = "cancelAllOrdersAfterStatus";
    }

    public enum CHANNEL {
        TICKER("ticker", "public", TickerMessage.class),
        OHLC("ohlc", "public", OHLCMessage.class),
        TRADE("trade", "public", TradeMessage.class),
        SPREAD("spread", "public", SpreadMessage.class),
        BOOK("book", "public", BaseBookMessage.class),
        OWN_TRADES("ownTrades", "private", OwnTradesMessage.class),
        OPEN_ORDERS("openOrders", "private", OpenOrdersMessage.class);

        private final String channel;
        private final String visibility;
        private final Class<? extends AbstractPublicationMessage> publicationMessageClass;
        private static final Map<String, CHANNEL> CHANNEL_MAP = new HashMap<>();

        CHANNEL(String channel, String visibility, Class<? extends AbstractPublicationMessage> publicationMessageClass) {
            this.channel = channel;
            this.visibility = visibility;
            this.publicationMessageClass = publicationMessageClass;
        }

        static {
            for (CHANNEL e : CHANNEL.values()) {
                CHANNEL_MAP.put(e.getVChannel(), e);
            }
        }

        @JsonValue
        public String getVChannel() {
            return this.channel;
        }

        public String getVisibility() {
            return this.visibility;
        }

        public Class<? extends AbstractPublicationMessage> getPublicationMessageClass() {
            return this.publicationMessageClass;
        }

        public static CHANNEL getEChannel(String channel) {
            return CHANNEL_MAP.get(channel);
        }
    }

    public enum SYSTEM_STATUS_ENUM {
        ONLINE("online"),
        MAINTENANCE("maintenance"),
        CANCEL_ONLY("cancel_only"),
        LIMIT_ONLY("limit_only"),
        POST_ONLY("post_only");

        private final String systemStatus;
        private static final Map<String, SYSTEM_STATUS_ENUM> SYSTEM_STATUS_MAP = new HashMap<>();

        SYSTEM_STATUS_ENUM(String systemStatus) {
            this.systemStatus = systemStatus;
        }

        static {
            for (SYSTEM_STATUS_ENUM e : SYSTEM_STATUS_ENUM.values()) {
                SYSTEM_STATUS_MAP.put(e.getVSystemStatus(), e);
            }
        }

        @JsonValue
        public String getVSystemStatus() {
            return this.systemStatus;
        }

        public static SYSTEM_STATUS_ENUM getESystemStatus(String systemStatus) {
            return SYSTEM_STATUS_MAP.get(systemStatus);
        }
    }

    public enum SUBSCRIPTION_STATUS {
        SUBSCRIBED("subscribed"),
        UNSUBSCRIBED("unsubscribed"),
        ERROR("error");

        private final String subscriptionStatus;
        private static final Map<String, SUBSCRIPTION_STATUS> SUBSCRIPTION_STATUS_MAP = new HashMap<>();

        SUBSCRIPTION_STATUS(String subscriptionStatus) {
            this.subscriptionStatus = subscriptionStatus;
        }

        static {
            for (SUBSCRIPTION_STATUS e : SUBSCRIPTION_STATUS.values()) {
                SUBSCRIPTION_STATUS_MAP.put(e.getVSubscriptionStatus(), e);
            }
        }

        @JsonValue
        public String getVSubscriptionStatus() {
            return this.subscriptionStatus;
        }

        public static SUBSCRIPTION_STATUS getESubscriptionStatus(String subscriptionStatus) {
            return SUBSCRIPTION_STATUS_MAP.get(subscriptionStatus);
        }
    }
}
