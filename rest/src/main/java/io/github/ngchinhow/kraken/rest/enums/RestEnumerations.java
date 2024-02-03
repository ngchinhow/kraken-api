package io.github.ngchinhow.kraken.rest.enums;

import io.github.ngchinhow.kraken.rest.client.*;

/**
 * Stores all enumerations related to Kraken's REST API
 */
public abstract class RestEnumerations {

    /**
     * Lists Clients as categorized by Kraken
     */
    public static class Endpoint {
        public static final Class<MarketDataClient> MARKET_DATA = MarketDataClient.class;
        public static final Class<UserDataClient> USER_DATA = UserDataClient.class;
        public static final Class<UserTradingClient> USER_TRADING = UserTradingClient.class;
        public static final Class<UserFundingClient> USER_FUNDING = UserFundingClient.class;
        public static final Class<UserSubaccountsClient> USER_SUBACCOUNTS = UserSubaccountsClient.class;
        public static final Class<UserStakingClient> USER_STAKING = UserStakingClient.class;
        public static final Class<WebSocketsAuthenticationClient> WEBSOCKETS_AUTHENTICATION = WebSocketsAuthenticationClient.class;
    }
}
