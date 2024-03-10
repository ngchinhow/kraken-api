package io.github.ngchinhow.kraken.rest.client;

import feign.Headers;
import feign.RequestLine;
import io.github.ngchinhow.kraken.rest.model.accountdata.balance.AccountBalanceResult;
import io.github.ngchinhow.kraken.rest.model.accountdata.balance.ExtendedBalanceResult;
import io.github.ngchinhow.kraken.rest.model.accountdata.balance.TradeBalanceRequest;
import io.github.ngchinhow.kraken.rest.model.accountdata.balance.TradeBalanceResult;
import io.github.ngchinhow.kraken.rest.model.userdata.openorders.OpenOrdersRequest;
import io.github.ngchinhow.kraken.rest.model.userdata.openorders.OpenOrdersResult;

public interface AccountDataClient extends RestClient {

    @RequestLine("POST /0/private/Balance")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    AccountBalanceResult getAccountBalance();

    @RequestLine("POST /0/private/BalanceEx")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    ExtendedBalanceResult getExtendedBalance();
    @RequestLine("POST /0/private/TradeBalance")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    TradeBalanceResult getTradeBalance(TradeBalanceRequest request);


    @RequestLine("POST /0/private/OpenOrders")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    OpenOrdersResult getOpenOrders(OpenOrdersRequest request);
}
