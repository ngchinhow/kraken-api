package org.trading.krakenapi.websocket.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.trading.krakenapi.websocket.dto.publication.*;

import java.util.HashMap;
import java.util.Map;

public class WebSocketEnumerations {
    public static final String PING_EVENT = "ping";
    public static final String PONG_EVENT = "pong";
    public static final String HEARTBEAT_EVENT = "heartbeat";
    public static final String SYSTEM_STATUS_EVENT = "systemStatus";
    public static final String SUBSCRIBE_EVENT = "subscribe";
    public static final String UNSUBSCRIBE_EVENT = "unsubscribe";
    public static final String SUBSCRIPTION_STATUS_EVENT = "subscriptionStatus";
    public static final String ADD_ODER_EVENT = "addOrder";
    public static final String ADD_ORDER_STATUS_EVENT = "addOrderStatus";
    public static final String EDIT_ORDER_EVENT = "editOrder";
    public static final String EDIT_ORDER_STATUS_EVENT = "editOrderStatus";
    public static final String CANCEL_ORDER_EVENT = "cancelOrder";
    public static final String CANCEL_ORDER_STATUS_EVENT = "cancelOrderStatus";
    public static final String CANCEL_ALL_EVENT = "cancelAll";
    public static final String CANCEL_ALL_STATUS_EVENT = "cancelAllStatus";
    public static final String CANCEL_ALL_ORDERS_AFTER_EVENT = "cancelAllOrdersAfter";
    public static final String CANCEL_ALL_ORDERS_AFTER_STATUS_EVENT = "cancelAllOrdersAfterStatus";

    public enum CHANNEL {
        TICKER("ticker", "public", TickerMessage.class),
        OHLC("ohlc", "public", OHLCMessage.class),
        TRADE("trade", "public", TradeMessage.class),
        SPREAD("spread", "public", SpreadMessage.class),
        BOOK("book", "public", BookMessage.class),
        OWN_TRADES("ownTrades", "private", null),
        OPEN_ORDERS("openOrders", "private", null);

        private final String channel;
        private final String visibility;
        private final Class<? extends PublicationMessage> publicationMessageClass;
        private static final Map<String, CHANNEL> CHANNEL_MAP = new HashMap<>();

        CHANNEL(String channel, String visibility, Class<? extends PublicationMessage> publicationMessageClass) {
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

        public Class<? extends PublicationMessage> getPublicationMessageClass() {
            return this.publicationMessageClass;
        }

        public static CHANNEL getEChannel(String channel) {
            return CHANNEL_MAP.get(channel);
        }
    }

    public enum SYSTEM_STATUS {
        ONLINE("online"),
        MAINTENANCE("maintenance"),
        CANCEL_ONLY("cancel_only"),
        LIMIT_ONLY("limit_only"),
        POST_ONLY("post_only");

        private final String systemStatus;
        private static final Map<String, SYSTEM_STATUS> SYSTEM_STATUS_MAP = new HashMap<>();

        SYSTEM_STATUS(String systemStatus) {
            this.systemStatus = systemStatus;
        }

        static {
            for (SYSTEM_STATUS e : SYSTEM_STATUS.values()) {
                SYSTEM_STATUS_MAP.put(e.getVSystemStatus(), e);
            }
        }

        @JsonValue
        public String getVSystemStatus() {
            return this.systemStatus;
        }

        public static SYSTEM_STATUS getESystemStatus(String systemStatus) {
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
