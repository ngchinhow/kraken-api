package io.github.ngchinhow.kraken.rest.client;

import io.github.ngchinhow.kraken.rest.model.KrakenResponse;
import io.github.ngchinhow.kraken.rest.model.marketdata.ServerTime;
import feign.QueryMap;
import feign.RequestLine;
import io.github.ngchinhow.kraken.rest.model.marketdata.TradableAssetPairs;

public interface MarketDataClient extends RestClient {
    @RequestLine("GET /0/public/Time")
    KrakenResponse<ServerTime.Result> getServerTime();

    @RequestLine("GET /0/public/AssetPairs")
    KrakenResponse<TradableAssetPairs.Result> getTradeableAssetPairs(@QueryMap TradableAssetPairs.Request request);
}
