package io.github.ngchinhow.kraken.rest.client;

import io.github.ngchinhow.kraken.rest.factory.RestClientFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TradingClientTest {

    private static TradingClient CLIENT;

    @BeforeAll
    static void beforeAll() {
        CLIENT = RestClientFactory.getPrivateRestClient(
            TradingClient.class,
            System.getenv("KRAKEN_REST_API_KEY"),
            System.getenv("KRAKEN_REST_PRIVATE_KEY"));
    }

    @Test
    public void givenUserTradingClient_whenAddOrder_thenSucceed() {
        CLIENT.addOrder("limit", "XBTUSD", 37500, "buy", 1.25);
    }
}
