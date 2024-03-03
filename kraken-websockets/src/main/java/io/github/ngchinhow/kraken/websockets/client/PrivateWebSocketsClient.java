package io.github.ngchinhow.kraken.websockets.client;

import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.rest.client.WebSocketsAuthenticationClient;
import io.github.ngchinhow.kraken.websockets.model.method.ParameterInterface;
import io.github.ngchinhow.kraken.websockets.model.method.PrivateParameterInterface;
import io.github.ngchinhow.kraken.websockets.model.method.order.add.AddOrderRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.add.AddOrderResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.batchadd.BatchAddOrdersRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.batchadd.BatchAddOrdersResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.batchcancel.BatchCancelOrdersRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.batchcancel.BatchCancelOrdersResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancel.CancelOrderRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancel.CancelOrderResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancelall.CancelAllOrdersRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancelall.CancelAllOrdersResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancelallafter.CancelAllOrdersAfterRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancelallafter.CancelAllOrdersAfterResponse;
import io.github.ngchinhow.kraken.websockets.model.method.order.edit.EditOrderRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.edit.EditOrderResponse;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static io.github.ngchinhow.kraken.websockets.properties.WebSocketsProperties.PRIVATE_API_URL;

@Slf4j
public final class PrivateWebSocketsClient extends BaseWebSocketsClient {
    private final WebSocketsAuthenticationClient authenticationClient;

    public PrivateWebSocketsClient(MarketDataClient marketDataClient,
                                   WebSocketsAuthenticationClient authenticationClient) {
        super(PRIVATE_API_URL, marketDataClient);
        this.authenticationClient = authenticationClient;
    }

    public Single<AddOrderResponse> addOrder(AddOrderRequest request) {
        return sendDirectRequest(request);
    }

    public Single<BatchAddOrdersResponse> batchAddOrders(BatchAddOrdersRequest request) {
        return sendDirectRequest(request);
    }

    public Single<BatchCancelOrdersResponse> batchCancelOrders(BatchCancelOrdersRequest request) {
        return sendDirectRequest(request);
    }

    public Single<CancelAllOrdersResponse> cancelAllOrders(CancelAllOrdersRequest request) {
        return sendDirectRequest(request);
    }

    public Single<CancelAllOrdersAfterResponse> cancelAllOrdersAfter(CancelAllOrdersAfterRequest request) {
        return sendDirectRequest(request);
    }

    public List<Single<CancelOrderResponse>> cancelOrder(CancelOrderRequest request) {
        return sendExpandingRequest(request);
    }

    public Single<EditOrderResponse> editOrder(EditOrderRequest request) {
        return sendDirectRequest(request);
    }

    @Override
    protected void checkPrivateParameter(ParameterInterface param) {
        if (param instanceof PrivateParameterInterface privateParam)
            addTokenToParameter(privateParam);
    }

    /**
     * Add WebSockets token
     *
     * @param param Private parameter requiring a WebSockets token
     */
    private void addTokenToParameter(PrivateParameterInterface param) {
        final var token = authenticationClient.getWebsocketsToken().getToken();
        param.setToken(token);
    }
}
