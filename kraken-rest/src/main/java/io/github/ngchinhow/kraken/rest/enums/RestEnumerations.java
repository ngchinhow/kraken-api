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
        public static final Class<AccountDataClient> USER_DATA = AccountDataClient.class;
        public static final Class<TradingClient> USER_TRADING = TradingClient.class;
        public static final Class<FundingClient> USER_FUNDING = FundingClient.class;
        public static final Class<SubAccountsClient> USER_SUBACCOUNTS = SubAccountsClient.class;
        public static final Class<StakingClient> USER_STAKING = StakingClient.class;
        public static final Class<WebSocketsAuthenticationClient> WEBSOCKETS_AUTHENTICATION = WebSocketsAuthenticationClient.class;
    }
}
