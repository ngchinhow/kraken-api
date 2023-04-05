package com.kraken.api.javawrapper.websocket;

import com.kraken.api.javawrapper.manager.KrakenConnectionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.kraken.api.javawrapper.rest.enums.RestEnumerations.Endpoint.*;

public class WebsocketConnectionManagerTest {
    private static final KrakenConnectionManager KRAKEN_CONNECTION_MANAGER = new KrakenConnectionManager(
        System.getenv("KRAKEN_REST_API_KEY"),
        System.getenv("KRAKEN_REST_PRIVATE_KEY")
    );

    @Test
    public void givenCredentials_whenGettingRestClients_thenSuccess() {
        Assertions.assertNotNull(KRAKEN_CONNECTION_MANAGER);
        Assertions.assertNotNull(KRAKEN_CONNECTION_MANAGER.getRestClient(MARKET_DATA));
        Assertions.assertNotNull(KRAKEN_CONNECTION_MANAGER.getRestClient(USER_DATA));
        Assertions.assertNotNull(KRAKEN_CONNECTION_MANAGER.getRestClient(USER_TRADING));
        Assertions.assertNotNull(KRAKEN_CONNECTION_MANAGER.getRestClient(USER_FUNDING));
        Assertions.assertNotNull(KRAKEN_CONNECTION_MANAGER.getRestClient(USER_SUBACCOUNTS));
        Assertions.assertNotNull(KRAKEN_CONNECTION_MANAGER.getRestClient(USER_STAKING));
        Assertions.assertNotNull(KRAKEN_CONNECTION_MANAGER.getRestClient(WEBSOCKETS_AUTHENTICATION));
    }

    @Test
    public void givenCredentials_whenGettingWebSocketsClient_thenSuccess() {
        Assertions.assertNotNull(KRAKEN_CONNECTION_MANAGER.getKrakenPublicWebSocketClient());
        Assertions.assertNotNull(KRAKEN_CONNECTION_MANAGER.getKrakenPrivateWebSocketClient());
    }
}
