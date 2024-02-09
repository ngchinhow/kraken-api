package io.github.ngchinhow.kraken.rest.client;

import feign.Headers;
import feign.RequestLine;
import io.github.ngchinhow.kraken.rest.model.userdata.account.AccountBalanceResult;
import io.github.ngchinhow.kraken.rest.model.userdata.openorders.OpenOrdersRequest;
import io.github.ngchinhow.kraken.rest.model.userdata.openorders.OpenOrdersResult;

public interface UserDataClient extends RestClient {


    @RequestLine("POST /0/private/Balance")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    AccountBalanceResult getAccountBalance();

    @RequestLine("POST /0/private/OpenOrders")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    OpenOrdersResult getOpenOrders(OpenOrdersRequest request);
}
