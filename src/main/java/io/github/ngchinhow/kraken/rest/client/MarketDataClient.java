package io.github.ngchinhow.kraken.rest.client;

import io.github.ngchinhow.kraken.rest.dto.KrakenResponse;
import io.github.ngchinhow.kraken.rest.dto.marketdata.ServerTimeResult;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

public interface MarketDataClient extends RestClient {
    @RequestLine("GET /public/Time")
    KrakenResponse<ServerTimeResult> getServerTime();

    @RequestLine("GET /public/AssetPairs")
    void getTradeableAssetPairs(@QueryMap Map<String, Object> queryMap);
}
