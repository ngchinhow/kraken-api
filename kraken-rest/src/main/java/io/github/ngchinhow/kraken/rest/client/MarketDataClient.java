package io.github.ngchinhow.kraken.rest.client;

import feign.QueryMap;
import feign.RequestLine;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.asset.AssetRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.asset.AssetResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.book.OrderBookRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.book.OrderBookResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.ohlc.OHLCRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.ohlc.OHLCResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.pair.TradableAssetPairRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.pair.TradableAssetPairResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.servertime.ServerTimeResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.spreads.RecentSpreadsRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.spreads.RecentSpreadsResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.system.SystemStatusResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.ticker.TickerRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.ticker.TickerResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.trades.RecentTradesRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.spot.trades.RecentTradesResult;

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

    @RequestLine("GET /0/public/Spread")
    RecentSpreadsResult getRecentSpreads(@QueryMap RecentSpreadsRequest request);
}
