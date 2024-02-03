package io.github.ngchinhow.kraken.rest.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface UserTradingClient extends RestClient {

    @RequestLine("POST /0/private/AddOrder")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    void addOrder(
        @Param("ordertype") String orderType,
        @Param("pair") String pair,
        @Param("price") int price,
        @Param("type") String type,
        @Param("volume") double volume
    );
}
