package org.trading.krakenapi.rest.enums;

import org.trading.krakenapi.rest.client.*;

public class RestEnumerations {
    public enum ENDPOINT {
        MARKET_DATA(MarketDataClient.class),
        USER_DATA(UserDataClient.class),
        USER_TRADING(UserTradingClient.class),
        USER_FUNDING(UserFundingClient.class),
        USER_SUBACCOUNTS(UserSubaccountsClient.class),
        USER_STAKING(UserStakingClient.class),
        WEBSOCKET_AUTHENTICATION(WebSocketsAuthenticationClient.class);

        private final Class<? extends RestClient> restClientClass;

        ENDPOINT(Class<? extends RestClient> restClientClass) {
            this.restClientClass = restClientClass;
        }

        public Class<? extends RestClient> getRestClientClass() {
            return this.restClientClass;
        }
    }
}
