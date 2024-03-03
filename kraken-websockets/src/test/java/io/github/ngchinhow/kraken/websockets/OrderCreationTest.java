package io.github.ngchinhow.kraken.websockets;

import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderOutput;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderParameter;
import io.github.ngchinhow.kraken.websockets.model.method.order.add.AddOrderResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.batchadd.BatchAddOrdersResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancelall.CancelAllOrdersRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.edit.EditOrderResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.edit.EditOrderResult;
import io.github.ngchinhow.kraken.websockets.utils.Helper;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class OrderCreationTest extends BasePrivateWebSocketsClientTest {

    private final CancelAllOrdersRequest cancelAllOrdersRequest = CancelAllOrdersRequest.builder()
                                                                                        .params(new BaseOrderParameter())
                                                                                        .build();

    @AfterEach
    void cleanup() {
        client.cancelAllOrders(cancelAllOrdersRequest).blockingSubscribe();
    }

    @Test
    void givenPrivateWebSocketsClient_whenAddOrder_thenSucceed() {
        final var reqId = BigInteger.TEN;
        final var orderUserReference = BigInteger.ONE;
        final var addOrderRequest = Helper.buildStandardAddOrderRequest(reqId, orderUserReference);

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

    @Test
    void givenPrivateWebSocketsClient_whenBatchAddOrders_thenSucceed() {
        final var reqId = BigInteger.TEN;
        final var orderUserReference = BigInteger.ONE;
        final var batchAddOrdersRequest = Helper.buildStandardBatchAddOrdersRequest(reqId, orderUserReference);

        final var response = client.batchAddOrders(batchAddOrdersRequest).blockingGet();

        assertThat(response)
            .isNotNull()
            .returns(reqId, from(BatchAddOrdersResponse::getRequestId))
            .returns(true, from(BatchAddOrdersResponse::getSuccess))
            .extracting(BatchAddOrdersResponse::getResult)
            .asInstanceOf(InstanceOfAssertFactories.list(BaseOrderOutput.class))
            .isNotEmpty()
            .allSatisfy(r -> assertThat(r)
                .isNotNull()
                .doesNotReturn(null, from(BaseOrderOutput::getOrderId))
                .returns(orderUserReference, from(BaseOrderOutput::getOrderUserReference)));

        assertThat(client.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .isEmpty();
    }

    @Test
    void givenPrivateWebSocketsClient_whenEditOrder_thenSucceed() {
        final var reqId = BigInteger.TEN;
        final var orderUserReference = BigInteger.ONE;
        final var addOrderRequest = Helper.buildStandardAddOrderRequest(reqId, orderUserReference);
        final var orderId = client.addOrder(addOrderRequest)
                                  .blockingGet()
                                  .getResult()
                                  .getOrderId();
        final var editOrderRequest = Helper.buildStandardEditOrderRequest(reqId, orderId);

        final var response = client.editOrder(editOrderRequest).blockingGet();

        assertThat(response)
            .isNotNull()
            .returns(reqId, from(EditOrderResponse::getRequestId))
            .returns(true, from(EditOrderResponse::getSuccess))
            .extracting(EditOrderResponse::getResult)
            .doesNotReturn(null, from(EditOrderResult::getOrderId))
            .returns(orderId, from(EditOrderResult::getOriginalOrderId));

        assertThat(client.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .isEmpty();
    }
}
