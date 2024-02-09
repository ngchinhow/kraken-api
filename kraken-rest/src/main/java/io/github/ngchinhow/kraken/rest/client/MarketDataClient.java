package io.github.ngchinhow.kraken.rest.client;

import feign.QueryMap;
import feign.RequestLine;
import io.github.ngchinhow.kraken.rest.model.marketdata.asset.AssetRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.asset.AssetResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.ohlc.OHLCRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.ohlc.OHLCResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.pair.TradableAssetPairRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.pair.TradableAssetPairResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.servertime.ServerTimeResult;

public interface MarketDataClient extends RestClient {
    @RequestLine("GET /0/public/Time")
    ServerTimeResult getServerTime();

    @RequestLine("GET /0/public/Assets")
    AssetResult getAssetInformation(@QueryMap AssetRequest request);

    @RequestLine("GET /0/public/AssetPairs")
    TradableAssetPairResult getTradableAssetPairs(@QueryMap TradableAssetPairRequest request);

    @RequestLine("GET /0/public/OHLC")
    OHLCResult getOHLCData(@QueryMap OHLCRequest request);
}
