package io.github.ngchinhow.kraken.rest.client;

import feign.FeignException;
import io.github.ngchinhow.kraken.rest.factory.RestClientFactory;
import io.github.ngchinhow.kraken.rest.model.marketdata.asset.AssetRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.asset.AssetResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.book.OrderBookRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.book.OrderBookResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.ohlc.OHLCRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.ohlc.OHLCResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.pair.TradableAssetPairRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.pair.TradableAssetPairResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.servertime.ServerTimeResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.spreads.RecentSpreadsRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.spreads.RecentSpreadsResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.system.SystemStatusResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.ticker.TickerRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.ticker.TickerResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.trades.RecentTradesRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.trades.RecentTradesResult;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarketDataClientTest {
    private static MarketDataClient CLIENT;

    @BeforeAll
    static void beforeAll() {
        CLIENT = RestClientFactory.getPublicRestClient(MarketDataClient.class);
    }

    @Test
    void givenMarketDataClient_whenGetServerTime_thenSucceed() {
        ServerTimeResult response = CLIENT.getServerTime();
        assertThat(response)
            .isNotNull()
            .extracting(ServerTimeResult::getIsoTime)
            .isNotNull();
    }

    @Test
    void givenMarketDataClient_whenGetSystemStatus_thenSucceed() {
        final var response = CLIENT.getSystemStatus();

        assertThat(response)
            .isNotNull()
            .doesNotReturn(null, from(SystemStatusResult::status))
            .doesNotReturn(null, from(SystemStatusResult::timestamp));
    }

    @Test
    void givenMarketDataClient_whenGetAssetInformation_thenSucceed() {
        List<String> pairs = List.of("XXBT", "ZUSD", "XETH");
        AssetRequest request = AssetRequest.builder()
                                           .pairs(pairs)
                                           .build();
        AssetResult result = CLIENT.getAssetInformation(request);
        assertThat(result)
            .isNotNull()
            .extracting(AssetResult::getAssets, InstanceOfAssertFactories.map(String.class, AssetResult.Asset.class))
            .hasSize(3)
            .allSatisfy((name, asset) -> {
                assertThat(asset).isNotNull();
                assertTrue(pairs.contains(name) || pairs.contains(asset.getAlternateName()));
            });
    }

    @Test
    void givenMarketDataClient_whenGetAssetInformationWithInvalidAsset_thenFail() {
        List<String> pairs = List.of("ABC123");
        AssetRequest request = AssetRequest.builder()
                                           .pairs(pairs)
                                           .build();
        assertThatExceptionOfType(FeignException.BadRequest.class)
            .isThrownBy(() -> CLIENT.getAssetInformation(request))
            .withMessage("EQuery:Unknown asset");
    }

    @Test
    void givenMarketDataClient_whenGetTradableAssetPairs_thenSucceed() {
        List<String> pairs = List.of("XXBTZUSD", "XETHXXBT");
        TradableAssetPairRequest request = TradableAssetPairRequest.builder()
                                                                   .pairs(pairs)
                                                                   .build();
        TradableAssetPairResult result = CLIENT.getTradableAssetPairs(request);
        assertThat(result)
            .isNotNull()
            .extracting(TradableAssetPairResult::getAssetPairs,
                        InstanceOfAssertFactories.map(String.class, TradableAssetPairResult.AssetPair.class))
            .hasSize(2)
            .allSatisfy((pairName, assetPair) -> {
                assertThat(pairName)
                    .isIn(pairs);
                assertThat(assetPair)
                    .isNotNull()
                    .extracting(TradableAssetPairResult.AssetPair::getTakerFees, InstanceOfAssertFactories.LIST)
                    .isNotEmpty();
            });
    }

    @Test
    void givenMarketDataClient_whenGetTradableAssetPairsWithInvalidPair_thenFail() {
        List<String> pairs = List.of("ABC/123");
        TradableAssetPairRequest request = TradableAssetPairRequest.builder()
                                                                   .pairs(pairs)
                                                                   .build();
        assertThatExceptionOfType(FeignException.BadRequest.class)
            .isThrownBy(() -> CLIENT.getTradableAssetPairs(request))
            .withMessage("EQuery:Unknown asset pair");
    }

    @Test
    void givenMarketDataClient_whenGetTickerInformation_thenSucceed() {
        final var request = TickerRequest.builder().build();
        final var response = CLIENT.getTickerInformation(request);

        assertThat(response)
            .isNotNull()
            .extracting(from(TickerResult::getAssetTickerInfoMap), InstanceOfAssertFactories.MAP)
            .isNotEmpty();
    }

    @Test
    void givenMarketDataClient_whenGetTickerInformationForOnePair_thenOnlyOneResult() {
        final var pair = "PEPE/USD";
        final var request = TickerRequest.builder()
                                         .pair(pair)
                                         .build();

        final var response = CLIENT.getTickerInformation(request);

        assertThat(response)
            .isNotNull()
            .extracting(from(TickerResult::getAssetTickerInfoMap), InstanceOfAssertFactories.MAP)
            .containsOnlyKeys(pair);
    }

    @Test
    void givenMarketDataClient_whenGetTickerInformationWithInvalidPair_thenFail() {
        final var pair = "ABC/123";
        final var request = TickerRequest.builder()
                                         .pair(pair)
                                         .build();
        assertThatExceptionOfType(FeignException.BadRequest.class)
            .isThrownBy(() -> CLIENT.getTickerInformation(request))
            .withMessage("EQuery:Unknown asset pair");
    }

    @Test
    void givenMarketDataClient_whenGetOHLCData_thenSucceed() {
        OHLCRequest request = OHLCRequest.builder()
                                         .pair("XXBTZUSD")
                                         .build();
        OHLCResult result = CLIENT.getOHLCData(request);
        assertThat(result)
            .isNotNull()
            .doesNotReturn(null, from(OHLCResult::getAssetPairName))
            .extracting(OHLCResult::getOhlcList, InstanceOfAssertFactories.LIST)
            .isNotEmpty();
    }

    @Test
    void givenMarketDataClient_whenGetOHLCDataWithInvalidPair_thenFail() {
        OHLCRequest request = OHLCRequest.builder()
                                         .pair("ABC/123")
                                         .build();
        assertThatExceptionOfType(FeignException.BadRequest.class)
            .isThrownBy(() -> CLIENT.getOHLCData(request))
            .withMessage("EQuery:Unknown asset pair");
    }

    @Test
    void givenMarketDataClient_whenGetOrderBook_thenSucceed() {
        final var pair = "BTC/USD";
        final var request = OrderBookRequest.builder()
                                            .pair(pair)
                                            .build();

        final var response = CLIENT.getOrderBook(request);

        assertThat(response)
            .isNotNull()
            .returns(pair, from(OrderBookResult::getAssetPairName))
            .extracting(OrderBookResult::getOrderBook)
            .isNotNull();
    }

    @Test
    void givenMarketDataClient_whenGetOrderBookWithInvalidPair_thenFail() {
        final var request = OrderBookRequest.builder()
                                            .pair("ABC/123")
                                            .build();
        assertThatExceptionOfType(FeignException.BadRequest.class)
            .isThrownBy(() -> CLIENT.getOrderBook(request))
            .withMessage("EQuery:Unknown asset pair");
    }

    @Test
    void givenMarketDataClient_whenGetRecentTrades_thenSucceed() {
        final var pair = "BTC/USD";
        final var request = RecentTradesRequest.builder()
                                               .pair(pair)
                                               .build();

        final var response = CLIENT.getRecentTrades(request);

        assertThat(response)
            .isNotNull()
            .returns(pair, from(RecentTradesResult::getAssetPairName))
            .extracting(RecentTradesResult::getTickData, InstanceOfAssertFactories.LIST)
            .isNotEmpty();
    }

    @Test
    void givenMarketDataClient_whenGetRecentTradesWithSince_thenSucceed() {
        final var pair = "BTC/USD";
        final var request = RecentTradesRequest.builder()
                                               .pair(pair)
                                               .since(ZonedDateTime.of(2024, 2, 1, 0, 0, 0, 0, ZoneId.systemDefault()))
                                               .build();

        final var response = CLIENT.getRecentTrades(request);

        assertThat(response)
            .isNotNull()
            .returns(pair, from(RecentTradesResult::getAssetPairName))
            .extracting(RecentTradesResult::getTickData, InstanceOfAssertFactories.LIST)
            .isNotEmpty();
    }

    @Test
    void givenMarketDataClient_whenGetRecentTradesWithInvalidPair_thenFail() {
        final var request = RecentTradesRequest.builder()
                                               .pair("ABC/123")
                                               .build();
        assertThatExceptionOfType(FeignException.BadRequest.class)
            .isThrownBy(() -> CLIENT.getRecentTrades(request))
            .withMessage("EQuery:Unknown asset pair");
    }

    @Test
    void givenMarketDataClient_whenGetRecentSpreads_thenSucceed() {
        final var pair = "BTC/USD";
        final var request = RecentSpreadsRequest.builder()
                                                .pair(pair)
                                                .build();

        final var response = CLIENT.getRecentSpreads(request);

        assertThat(response)
            .isNotNull()
            .returns(pair, from(RecentSpreadsResult::getAssetPairName))
            .extracting(RecentSpreadsResult::getSpreadData, InstanceOfAssertFactories.LIST)
            .isNotEmpty();
    }

    @Test
    void givenMarketDataClient_whenGetRecentSpreadsWithSince_thenSucceed() {
        final var pair = "BTC/USD";
        final var request = RecentSpreadsRequest.builder()
                                                .pair(pair)
                                                .since(ZonedDateTime.of(2024, 2, 1, 0, 0, 0, 0, ZoneId.systemDefault()))
                                                .build();

        final var response = CLIENT.getRecentSpreads(request);

        assertThat(response)
            .isNotNull()
            .returns(pair, from(RecentSpreadsResult::getAssetPairName))
            .extracting(RecentSpreadsResult::getSpreadData, InstanceOfAssertFactories.LIST)
            .isNotEmpty();
    }

    @Test
    void givenMarketDataClient_whenGetRecentSpreadsWithInvalidPair_thenFail() {
        final var request = RecentSpreadsRequest.builder()
                                                .pair("ABC/123")
                                                .build();
        assertThatExceptionOfType(FeignException.BadRequest.class)
            .isThrownBy(() -> CLIENT.getRecentSpreads(request))
            .withMessage("EQuery:Unknown asset pair");
    }
}
