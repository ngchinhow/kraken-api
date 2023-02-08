package com.kraken.api.javawrapper.rest.client;

import com.kraken.api.javawrapper.rest.dto.KrakenResponse;
import com.kraken.api.javawrapper.rest.dto.marketdata.ServerTimeResult;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

public interface MarketDataClient extends RestClient {
    @RequestLine("GET /public/Time")
    KrakenResponse<ServerTimeResult> getServerTime();

    @RequestLine("GET /public/AssetPairs")
    void getTradeableAssetPairs(@QueryMap Map<String, Object> queryMap);
}
