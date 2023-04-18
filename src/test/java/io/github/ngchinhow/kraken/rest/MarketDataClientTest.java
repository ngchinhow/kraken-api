package io.github.ngchinhow.kraken.rest;

import io.github.ngchinhow.kraken.manager.KrakenConnectionManager;
import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.rest.model.KrakenResponse;
import io.github.ngchinhow.kraken.rest.model.marketdata.Assets;
import io.github.ngchinhow.kraken.rest.model.marketdata.OHLCData;
import io.github.ngchinhow.kraken.rest.model.marketdata.ServerTime;
import io.github.ngchinhow.kraken.rest.model.marketdata.TradableAssetPairs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MarketDataClientTest {

    private static final MarketDataClient MARKET_DATA_CLIENT = new KrakenConnectionManager(null, null)
        .getRestClient(MarketDataClient.class);

    @Test
    public void givenMarketDataClient_whenGetServerTime_thenSucceed() {
        KrakenResponse<ServerTime.Result> response = MARKET_DATA_CLIENT.getServerTime();
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getError().isEmpty());
        Assertions.assertNotNull(response.getResult().getIsoTime());
    }

    @Test
    public void givenMarketDataClient_whenGetAssetInformation_thenSucceed() {
        List<String> pairs = List.of("XXBT", "ZUSD", "XETH");
        Assets.Request request = Assets.Request.builder()
            .pairs(pairs)
            .build();
        KrakenResponse<Assets.Result> response = MARKET_DATA_CLIENT.getAssetInformation(request);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getError().isEmpty());
        Assets.Result result = response.getResult();
        Assertions.assertEquals(3, result.getAssets().size());
        result.getAssets().forEach((pairName, asset) -> {
            Assertions.assertTrue(pairs.contains(pairName));
            Assertions.assertNotNull(asset);
        });
    }

    @Test
    public void givenMarketDataClient_whenGetTradableAssetPairs_thenSucceed() {
        List<String> pairs = List.of("XXBTZUSD", "XETHXXBT");
        TradableAssetPairs.Request request = TradableAssetPairs.Request.builder()
            .pairs(pairs)
            .build();
        KrakenResponse<TradableAssetPairs.Result> response = MARKET_DATA_CLIENT.getTradeableAssetPairs(request);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getError().isEmpty());
        TradableAssetPairs.Result result = response.getResult();
        Assertions.assertEquals(2, result.getAssetPairs().size());
        result.getAssetPairs().forEach((pairName, assetPair) -> {
            Assertions.assertTrue(pairs.contains(pairName));
            Assertions.assertNotNull(assetPair);
            Assertions.assertFalse(assetPair.getTakerFees().isEmpty());
        });
    }

    @Test
    public void givenMarketDataClient_whenGetOHLCData_thenSucceed() {
        OHLCData.Request request = OHLCData.Request.builder()
            .pair("XXBTZUSD")
            .build();
        KrakenResponse<OHLCData.Result> response = MARKET_DATA_CLIENT.getOHLCData(request);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getError().isEmpty());
        OHLCData.Result result = response.getResult();
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getAssetPairName());
        Assertions.assertFalse(result.getTicks().isEmpty());
    }
}
