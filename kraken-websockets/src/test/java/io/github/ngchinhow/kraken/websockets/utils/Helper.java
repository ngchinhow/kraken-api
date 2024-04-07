package io.github.ngchinhow.kraken.websockets.utils;

import io.github.ngchinhow.kraken.common.enumerations.Side;
import io.github.ngchinhow.kraken.websockets.model.method.channel.book.BookParameter;
import io.github.ngchinhow.kraken.websockets.model.method.channel.instrument.InstrumentParameter;
import io.github.ngchinhow.kraken.websockets.model.method.channel.ohlc.OHLCParameter;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderCreationInput;
import io.github.ngchinhow.kraken.websockets.model.method.order.BaseOrderParameter;
import io.github.ngchinhow.kraken.websockets.model.method.order.add.AddOrderParameter;
import io.github.ngchinhow.kraken.websockets.model.method.order.add.AddOrderRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.batchadd.BatchAddOrdersParameter;
import io.github.ngchinhow.kraken.websockets.model.method.order.batchadd.BatchAddOrdersRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.batchcancel.BatchCancelOrdersParameter;
import io.github.ngchinhow.kraken.websockets.model.method.order.batchcancel.BatchCancelOrdersRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancel.CancelOrderParameter;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancel.CancelOrderRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancelall.CancelAllOrdersRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancelallafter.CancelAllOrdersAfterParameter;
import io.github.ngchinhow.kraken.websockets.model.method.order.cancelallafter.CancelAllOrdersAfterRequest;
import io.github.ngchinhow.kraken.websockets.model.method.order.edit.EditOrderParameter;
import io.github.ngchinhow.kraken.websockets.model.method.order.edit.EditOrderRequest;
import io.github.ngchinhow.kraken.websockets.model.method.subscription.SubscribeRequest;
import io.github.ngchinhow.kraken.websockets.model.method.unsubscription.UnsubscribeRequest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public final class Helper {

    private Helper() throws IllegalAccessException {
        throw new IllegalAccessException("Static builders");
    }

    public static SubscribeRequest<BookParameter> buildStandardBookSubscribeRequest() {
        return SubscribeRequest.<BookParameter>builder()
                               .requestId(new BigInteger("12345"))
                               .params(BookParameter.builder()
                                                    .depth(10)
                                                    .symbols(List.of("BTC/USD", "BTC/EUR"))
                                                    .build())
                               .build();
    }

    public static UnsubscribeRequest<BookParameter> buildStandardBookUnsubscribeRequest() {
        return UnsubscribeRequest.<BookParameter>builder()
                                 .requestId(new BigInteger("12345"))
                                 .params(BookParameter.builder()
                                                      .depth(10)
                                                      .symbols(List.of("BTC/USD", "BTC/EUR"))
                                                      .build())
                                 .build();
    }

    public static SubscribeRequest<OHLCParameter> buildStandardOHLCSubscribeRequest() {
        return SubscribeRequest.<OHLCParameter>builder()
                               .requestId(new BigInteger("12345"))
                               .params(OHLCParameter.builder()
                                                    .snapshot(false)
                                                    .interval(30)
                                                    .symbols(List.of("BTC/USD", "BTC/EUR"))
                                                    .build())
                               .build();
    }

    public static SubscribeRequest<InstrumentParameter> buildStandardInstrumentSubscribeRequest() {
        return SubscribeRequest.<InstrumentParameter>builder()
                               .requestId(new BigInteger("12345"))
                               .params(InstrumentParameter.builder()
                                                          .snapshot(true)
                                                          .build())
                               .build();
    }

    public static AddOrderRequest buildStandardAddOrderRequest(BigInteger reqId, BigInteger orderUserReference) {
        final var addOrderParam = AddOrderParameter.builder()
                                                   .limitPrice(new BigDecimal(5000))
                                                   .orderType(BaseOrderCreationInput.OrderType.LIMIT)
                                                   .orderUserReference(orderUserReference)
                                                   .orderQuantity(new BigDecimal("0.0001"))
                                                   .side(Side.BUY)
                                                   .symbol("BTC/USD")
                                                   .validate(true)
                                                   .build();
        return AddOrderRequest.builder()
                              .requestId(reqId)
                              .params(addOrderParam)
                              .build();
    }

    public static BatchAddOrdersRequest buildStandardBatchAddOrdersRequest(BigInteger reqId,
                                                                           BigInteger orderUserReference) {
        final var order1 = BaseOrderCreationInput.builder()
                                                 .limitPrice(new BigDecimal(5000))
                                                 .orderType(BaseOrderCreationInput.OrderType.LIMIT)
                                                 .orderUserReference(orderUserReference)
                                                 .orderQuantity(new BigDecimal("0.0001"))
                                                 .side(Side.BUY)
                                                 .build();
        final var order2 = BaseOrderCreationInput.builder()
                                                 .limitPrice(new BigDecimal(5000))
                                                 .orderType(BaseOrderCreationInput.OrderType.LIMIT)
                                                 .orderUserReference(orderUserReference)
                                                 .orderQuantity(new BigDecimal("0.0001"))
                                                 .side(Side.BUY)
                                                 .build();
        final var batchAddOrderParam = BatchAddOrdersParameter.builder()
                                                              .orders(List.of(order1, order2))
                                                              .symbol("BTC/USD")
                                                              .validate(true)
                                                              .build();
        return BatchAddOrdersRequest.builder()
                                    .requestId(reqId)
                                    .params(batchAddOrderParam)
                                    .build();
    }

    public static BatchCancelOrdersRequest buildStandardBatchCancelOrdersRequest(BigInteger reqId,
                                                                                 List<String> orders) {
        final var batchCancelOrdersParam = BatchCancelOrdersParameter.builder()
                                                                     .orders(orders)
                                                                     .build();
        return BatchCancelOrdersRequest.builder()
                                       .requestId(reqId)
                                       .params(batchCancelOrdersParam)
                                       .build();
    }

    public static CancelAllOrdersRequest buildStandardCancelAllOrdersRequest(BigInteger reqId) {
        final var cancelAllOrdersParam = new BaseOrderParameter();
        return CancelAllOrdersRequest.builder()
                                     .requestId(reqId)
                                     .params(cancelAllOrdersParam)
                                     .build();
    }

    public static CancelAllOrdersAfterRequest buildStandardCancelAllOrdersAfterRequest(BigInteger reqId) {
        final var param = CancelAllOrdersAfterParameter.builder()
                                                       .timeout(5)
                                                       .build();
        return CancelAllOrdersAfterRequest.builder()
                                          .requestId(reqId)
                                          .params(param)
                                          .build();
    }

    public static CancelOrderRequest buildStandardCancelOrderRequest(BigInteger reqId, List<String> orderIds) {
        final var param = CancelOrderParameter.builder()
                                              .orderIds(orderIds)
                                              .build();
        return CancelOrderRequest.builder()
                                 .requestId(reqId)
                                 .params(param)
                                 .build();
    }

    public static EditOrderRequest buildStandardEditOrderRequest(BigInteger requestId, String orderId) {
        final var editOrderParam = EditOrderParameter.builder()
                                                     .orderId(orderId)
                                                     .symbol("BTC/USD")
                                                     .orderQuantity(new BigDecimal("0.0002"))
                                                     .validate(true)
                                                     .build();
        return EditOrderRequest.builder()
                               .requestId(requestId)
                               .params(editOrderParam)
                               .build();
    }
}
