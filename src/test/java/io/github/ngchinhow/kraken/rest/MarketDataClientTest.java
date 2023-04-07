package io.github.ngchinhow.kraken.rest;

import io.github.ngchinhow.kraken.manager.KrakenConnectionManager;
import io.github.ngchinhow.kraken.rest.client.MarketDataClient;
import io.github.ngchinhow.kraken.rest.model.KrakenResponse;
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
    public void givenMarketDataClient_whenGetTradableAssetPairs_thenSucceed() {
        TradableAssetPairs.Request request = TradableAssetPairs.Request.builder()
            .pairs(List.of("XXBTZUSD", "XETHXXBT"))
            .build();
        KrakenResponse<TradableAssetPairs.Result> response = MARKET_DATA_CLIENT.getTradeableAssetPairs(request);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getError().isEmpty());
        TradableAssetPairs.Result result = response.getResult();
        Assertions.assertEquals(2, result.getAssetPairs().size());
        result.getAssetPairs().forEach(p -> {
            Assertions.assertNotNull(p.getAssetPairName());
            Assertions.assertFalse(p.getTakerFees().isEmpty());
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
