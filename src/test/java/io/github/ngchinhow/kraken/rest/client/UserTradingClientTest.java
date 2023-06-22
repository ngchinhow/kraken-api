package io.github.ngchinhow.kraken.rest.client;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserTradingClientTest extends PrivateClientTest {

    private static UserTradingClient CLIENT;

    @BeforeAll
    public static void beforeAll() {
        CLIENT = KRAKEN_CONNECTION_MANAGER.getRestClient(UserTradingClient.class);
    }

    @Test
    public void givenUserTradingClient_whenAddOrder_thenSucceed() {
        CLIENT.addOrder("limit", "XBTUSD", 37500, "buy", 1.25);
    }
}
