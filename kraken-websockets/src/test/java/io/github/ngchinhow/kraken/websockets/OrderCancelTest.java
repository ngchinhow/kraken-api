package io.github.ngchinhow.kraken.websockets;

import io.github.ngchinhow.kraken.websockets.model.method.order.BaseCountResult;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderId;
import io.github.ngchinhow.kraken.websockets.model.method.order.batchcancel.BatchCancelOrdersResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancel.CancelOrderResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancelall.CancelAllOrdersResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancelallafter.CancelAllOrdersAfterResponse;
import io.github.ngchinhow.kraken.websockets.utils.Helper;
import io.reactivex.rxjava3.core.Single;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class OrderCancelTest extends BasePrivateWebSocketsClientTest {

    private List<String> orderIds;

    @BeforeEach
    void createOrders() {
        final var batchAddRequest = Helper.buildStandardBatchAddOrdersRequest(null, null);
        this.orderIds = client.batchAddOrders(batchAddRequest)
                              .blockingGet()
                              .getResult()
                              .stream()
                              .map(BaseOrderId::getOrderId)
                              .toList();
    }

    @Test
    void givenPrivateWebSocketsClient_whenBatchCancelOrders_thenSucceed() {
        final var reqId = BigInteger.TEN;
        final var numOrders = orderIds.size();
        final var batchCancelRequest = Helper.buildStandardBatchCancelOrdersRequest(reqId, orderIds);

        final var response = client.batchCancelOrders(batchCancelRequest).blockingGet();

        assertThat(response)
            .isNotNull()
            .returns(reqId, from(BatchCancelOrdersResponse::getRequestId))
            .returns(true, from(BatchCancelOrdersResponse::getSuccess))
            .returns(numOrders, from(BatchCancelOrdersResponse::getOrdersCancelled));

        assertThat(client.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .isEmpty();
    }

    @Test
    void givenPrivateWebSocketsClient_whenCancelAllOrders_thenSucceed() {
        final var reqId = BigInteger.TEN;
        final var numOrders = orderIds.size();
        final var request = Helper.buildStandardCancelAllOrdersRequest(reqId);

        final var response = client.cancelAllOrders(request).blockingGet();

        assertThat(response)
            .isNotNull()
            .returns(reqId, from(CancelAllOrdersResponse::getRequestId))
            .returns(true, from(CancelAllOrdersResponse::getSuccess))
            .extracting(CancelAllOrdersResponse::getResult)
            .isNotNull()
            .returns(numOrders, from(BaseCountResult::getCount));

        assertThat(client.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .isEmpty();
    }

    @Test
    void givenPrivateWebSocketsClient_whenCancelAllOrdersAfter_thenSucceed() {
        final var reqId = BigInteger.TEN;
        final var request = Helper.buildStandardCancelAllOrdersAfterRequest(reqId);

        final var response = client.cancelAllOrdersAfter(request).blockingGet();

        assertThat(response)
            .isNotNull()
            .returns(reqId, from(CancelAllOrdersAfterResponse::getRequestId))
            .returns(true, from(CancelAllOrdersAfterResponse::getSuccess))
            .extracting(CancelAllOrdersAfterResponse::getResult)
            .isNotNull()
            .returns((long) request.getParams().getTimeout(),
                     from(r -> ChronoUnit.SECONDS.between(r.getCurrentTime(), r.getTriggerTime())));

        assertThat(client.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .isEmpty();
    }

    @Test
    void givenPrivateWebSocketsClient_whenCancelOrder_thenSucceed() {
        final var reqId = BigInteger.TEN;
        final var request = Helper.buildStandardCancelOrderRequest(reqId, orderIds);

        final var response = client.cancelOrder(request);

        final var cancelledOrders = new ArrayList<String>();

        response.stream()
                .map(Single::blockingGet)
                .forEach(r -> {
                    assertThat(r)
                        .isNotNull()
                        .returns(reqId, from(CancelOrderResponse::getRequestId))
                        .returns(true, from(CancelOrderResponse::getSuccess))
                        .extracting(CancelOrderResponse::getResult)
                        .isNotNull();

                    cancelledOrders.add(r.getResult().getOrderId());
                });

        assertThat(cancelledOrders)
            .containsExactlyElementsOf(orderIds);

        assertThat(client.getWebSocketsTrafficGateway().getRequestsToResponsesMap())
            .isEmpty();
    }
}
