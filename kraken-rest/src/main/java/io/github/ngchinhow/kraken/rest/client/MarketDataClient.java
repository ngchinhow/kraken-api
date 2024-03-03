package io.github.ngchinhow.kraken.rest.client;

import feign.QueryMap;
import feign.RequestLine;
import io.github.ngchinhow.kraken.rest.model.marketdata.asset.AssetRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.asset.AssetResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.book.OrderBookRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.book.OrderBookResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.ohlc.OHLCRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.ohlc.OHLCResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.pair.TradableAssetPairRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.pair.TradableAssetPairResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.servertime.ServerTimeResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.system.SystemStatusResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.ticker.TickerRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.ticker.TickerResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.trades.RecentTradesRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.trades.RecentTradesResult;

public interface MarketDataClient extends RestClient {
    @RequestLine("GET /0/public/Time")
    ServerTimeResult getServerTime();

    @RequestLine("GET /0/public/SystemStatus")
    SystemStatusResult getSystemStatus();

    @RequestLine("GET /0/public/Assets")
    AssetResult getAssetInformation(@QueryMap AssetRequest request);

    @RequestLine("GET /0/public/AssetPairs")
    TradableAssetPairResult getTradableAssetPairs(@QueryMap TradableAssetPairRequest request);

    @RequestLine("GET /0/public/Ticker")
    TickerResult getTickerInformation(@QueryMap TickerRequest request);

    @RequestLine("GET /0/public/OHLC")
    OHLCResult getOHLCData(@QueryMap OHLCRequest request);

    @RequestLine("GET /0/public/Depth")
    OrderBookResult getOrderBook(@QueryMap OrderBookRequest request);

    @RequestLine("GET /0/public/Trades")
    RecentTradesResult getRecentTrades(@QueryMap RecentTradesRequest request);
}
