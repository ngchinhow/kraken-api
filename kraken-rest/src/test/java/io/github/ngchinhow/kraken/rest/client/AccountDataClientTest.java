package io.github.ngchinhow.kraken.rest.client;

import io.github.ngchinhow.kraken.rest.factory.RestClientFactory;
import io.github.ngchinhow.kraken.rest.model.accountdata.balance.TradeBalanceRequest;
import io.github.ngchinhow.kraken.rest.model.userdata.openorders.OpenOrdersRequest;
import io.github.ngchinhow.kraken.rest.model.userdata.openorders.OpenOrdersResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountDataClientTest {

    private static AccountDataClient CLIENT;

    @BeforeAll
    static void beforeAll() {
        CLIENT = RestClientFactory.getPrivateRestClient(AccountDataClient.class,
                                                        System.getenv("KRAKEN_REST_API_KEY"),
                                                        System.getenv("KRAKEN_REST_PRIVATE_KEY"));
    }

    @Test
    void givenAccountDataClient_whenGetAccountBalance_thenSucceed() {
        var result = CLIENT.getAccountBalance();
        assertThat(result)
            .isNotNull();
        System.out.println(result);
    }

    @Test
    void givenAccountDataClient_whenGetExtendedBalance_thenSucceed() {
        final var result = CLIENT.getExtendedBalance();

        assertThat(result.getAssets())
            .isNotEmpty();
    }

    @Test
    void givenAccountDataClient_whenGetTradeBalance_thenSucceed() {
        final var request = TradeBalanceRequest.builder()
                                               .build();
        final var result = CLIENT.getTradeBalance(request);

        assertThat(result)
            .hasNoNullFieldsOrPropertiesExcept("marginLevel");
    }

    @Test
    void givenAccountDataClient_whenGetOpenOrders_thenSucceed() {
        OpenOrdersRequest request = OpenOrdersRequest.builder()
                                                     .includeTrades(true)
                                                     .build();
        OpenOrdersResult result = CLIENT.getOpenOrders(request);
        assertThat(result)
            .isNotNull();
    }
}
