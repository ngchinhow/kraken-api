package com.kraken.api.javawrapper.websocket;

import com.kraken.api.javawrapper.manager.KrakenConnectionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.kraken.api.javawrapper.rest.enums.RestEnumerations.ENDPOINT.*;

public class WebsocketConnectionManagerTest {
    private static final String REST_API_KEY = System.getenv("KRAKEN_REST_API_KEY");
    private static final String REST_PRIVATE_KEY = System.getenv("KRAKEN_REST_PRIVATE_KEY");

    @Test
    public void givenCredentials_whenGettingRestClients_thenSuccess() {
        KrakenConnectionManager krakenConnectionManager = new KrakenConnectionManager(REST_API_KEY, REST_PRIVATE_KEY);
        Assertions.assertNotNull(krakenConnectionManager);
        Assertions.assertNotNull(krakenConnectionManager.getRestClient(MARKET_DATA));
        Assertions.assertNotNull(krakenConnectionManager.getRestClient(USER_DATA));
        Assertions.assertNotNull(krakenConnectionManager.getRestClient(USER_TRADING));
        Assertions.assertNotNull(krakenConnectionManager.getRestClient(USER_FUNDING));
        Assertions.assertNotNull(krakenConnectionManager.getRestClient(USER_SUBACCOUNTS));
        Assertions.assertNotNull(krakenConnectionManager.getRestClient(USER_STAKING));
        Assertions.assertNotNull(krakenConnectionManager.getRestClient(WEBSOCKETS_AUTHENTICATION));
    }

    @Test
    public void givenCredentials_whenGettingWebSocketsClient_thenSuccess() {

    }
}
