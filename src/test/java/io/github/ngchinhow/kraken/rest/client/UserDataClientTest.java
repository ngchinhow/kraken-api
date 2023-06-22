package io.github.ngchinhow.kraken.rest.client;

import io.github.ngchinhow.kraken.rest.model.userdata.openorders.OpenOrdersRequest;
import io.github.ngchinhow.kraken.rest.model.userdata.openorders.OpenOrdersResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserDataClientTest extends PrivateClientTest {

    private static UserDataClient CLIENT;

    @BeforeAll
    public static void beforeAll() {
        CLIENT = KRAKEN_CONNECTION_MANAGER.getRestClient(UserDataClient.class);
    }

    @Test
    public void givenUserDataClient_whenGetAccountBalance_thenSucceed() {
        var result = CLIENT.getAccountBalance();
        Assertions.assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void givenUserDataClient_whenGetOpenOrders_thenSucceed() {
        OpenOrdersRequest request = OpenOrdersRequest.builder()
            .includeTrades(true)
            .build();
        OpenOrdersResult result = CLIENT.getOpenOrders(request);
        Assertions.assertNotNull(result);
    }
}
