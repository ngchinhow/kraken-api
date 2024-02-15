package io.github.ngchinhow.kraken.websockets;

import io.github.ngchinhow.kraken.common.enumerations.Side;
import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.rest.client.WebSocketsAuthenticationClient;
import io.github.ngchinhow.kraken.rest.factory.RestClientFactory;
import io.github.ngchinhow.kraken.websockets.client.PrivateWebSocketsClient;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderCreationInput;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderOutput;
import io.github.ngchinhow.kraken.websockets.model.method.order.add.AddOrderParameter;
import io.github.ngchinhow.kraken.websockets.model.method.order.add.AddOrderRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.add.AddOrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class PrivateWebSocketsClientTest {
    private static final String KRAKEN_REST_API_KEY = System.getenv("KRAKEN_REST_API_KEY");
    private static final String KRAKEN_PRIVATE_API_KEY = System.getenv("KRAKEN_REST_PRIVATE_KEY");
    private static final MarketDataClient MARKET_DATA_CLIENT = RestClientFactory.getPublicRestClient(MarketDataClient.class);
    private static final WebSocketsAuthenticationClient WEB_SOCKETS_AUTHENTICATION_CLIENT = RestClientFactory.getPrivateRestClient(
        WebSocketsAuthenticationClient.class,
        KRAKEN_REST_API_KEY,
        KRAKEN_PRIVATE_API_KEY);
    private PrivateWebSocketsClient client;

    @BeforeEach
    void beforeEach() throws InterruptedException {
        client = new PrivateWebSocketsClient(MARKET_DATA_CLIENT, WEB_SOCKETS_AUTHENTICATION_CLIENT);
        client.connectBlocking();
    }

    @Test
    void givenPrivateWebSocketsClient_whenAddOrder_thenSucceed() {
        final var reqId = BigInteger.TEN;
        final var orderUserReference = BigInteger.ONE;
        final var addOrderParam = AddOrderParameter.builder()
                                                   .limitPrice(BigDecimal.ONE)
                                                   .orderType(BaseOrderCreationInput.OrderType.LIMIT)
                                                   .orderUserReference(orderUserReference)
                                                   .orderQuantity(new BigDecimal("0.000001"))
                                                   .side(Side.BUY)
                                                   .symbol("BTC/USD")
                                                   .build();
        final var addOrderRequest = AddOrderRequest.builder()
                                                   .requestId(reqId)
                                                   .params(addOrderParam)
                                                   .build();

        final var response = client.addOrder(addOrderRequest).blockingGet();

        assertThat(response)
            .isNotNull()
            .returns(reqId, from(AddOrderResponse::getRequestId))
            .returns(true, from(AddOrderResponse::getSuccess))
            .extracting(AddOrderResponse::getResult)
            .isNotNull()
            .doesNotReturn(null, from(BaseOrderOutput::getOrderId))
            .returns(orderUserReference, from(BaseOrderOutput::getOrderUserReference));

        assertThat(client.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .isEmpty();
    }
}
