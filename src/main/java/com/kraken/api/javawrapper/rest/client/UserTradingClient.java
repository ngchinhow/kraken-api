package com.kraken.api.javawrapper.rest.client;

import feign.Headers;
import feign.RequestLine;

import java.util.Map;

public interface UserTradingClient extends RestClient {

    @RequestLine("POST /private/AddOrder")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    void addOrder(Map<String, Object> form);
}
