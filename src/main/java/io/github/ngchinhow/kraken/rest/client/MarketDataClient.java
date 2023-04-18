package io.github.ngchinhow.kraken.rest.client;

import feign.QueryMap;
import feign.RequestLine;
import io.github.ngchinhow.kraken.rest.model.KrakenResponse;
import io.github.ngchinhow.kraken.rest.model.marketdata.Assets;
import io.github.ngchinhow.kraken.rest.model.marketdata.OHLCData;
import io.github.ngchinhow.kraken.rest.model.marketdata.ServerTime;
import io.github.ngchinhow.kraken.rest.model.marketdata.TradableAssetPairs;

public interface MarketDataClient extends RestClient {
    @RequestLine("GET /0/public/Time")
    KrakenResponse<ServerTime.Result> getServerTime();

    @RequestLine("GET /0/public/Assets")
    KrakenResponse<Assets.Result> getAssetInformation(@QueryMap Assets.Request request);

    @RequestLine("GET /0/public/AssetPairs")
    KrakenResponse<TradableAssetPairs.Result> getTradeableAssetPairs(@QueryMap TradableAssetPairs.Request request);

    @RequestLine("GET /0/public/OHLC")
    KrakenResponse<OHLCData.Result> getOHLCData(@QueryMap OHLCData.Request request);
}
