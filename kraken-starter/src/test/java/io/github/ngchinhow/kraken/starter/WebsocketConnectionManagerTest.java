package io.github.ngchinhow.kraken.starter;

import io.github.ngchinhow.kraken.rest.client.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class WebsocketConnectionManagerTest {
    private static final KrakenConnectionManager KRAKEN_CONNECTION_MANAGER = new KrakenConnectionManager(
        System.getenv("KRAKEN_REST_API_KEY"),
        System.getenv("KRAKEN_REST_PRIVATE_KEY")
    );

    @ParameterizedTest
    @ValueSource(classes = {MarketDataClient.class,
                            UserDataClient.class,
                            UserTradingClient.class,
                            UserFundingClient.class,
                            UserSubaccountsClient.class,
                            UserStakingClient.class,
                            WebSocketsAuthenticationClient.class})
    void givenCredentials_whenGettingRestClients_thenSuccess(Class<? extends RestClient> clazz) {
        assertThat(KRAKEN_CONNECTION_MANAGER)
            .isNotNull()
            .extracting(m -> m.getRestClient(clazz))
            .isNotNull();
    }

    @Test
    void givenCredentials_whenGettingWebSocketsClient_thenSuccess() {
        assertThat(KRAKEN_CONNECTION_MANAGER.getKrakenPublicWebSocketClient())
            .isNotNull();
        assertThat(KRAKEN_CONNECTION_MANAGER.getKrakenPrivateWebSocketClient())
            .isNotNull();
    }
}
