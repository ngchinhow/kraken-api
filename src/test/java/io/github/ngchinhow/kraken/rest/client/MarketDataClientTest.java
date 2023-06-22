package io.github.ngchinhow.kraken.rest.client;

import feign.FeignException;
import io.github.ngchinhow.kraken.rest.model.marketdata.asset.AssetRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.asset.AssetResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.ohlc.OHLCRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.ohlc.OHLCResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.pair.TradableAssetPairRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.pair.TradableAssetPairResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.servertime.ServerTimeResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MarketDataClientTest extends PublicClientTest {
    private static MarketDataClient CLIENT;

    @BeforeAll
    public static void beforeAll() {
        CLIENT = KRAKEN_CONNECTION_MANAGER.getRestClient(MarketDataClient.class);
    }

    @Test
    public void givenMarketDataClient_whenGetServerTime_thenSucceed() {
        ServerTimeResult response = CLIENT.getServerTime();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getIsoTime());
    }

    @Test
    public void givenMarketDataClient_whenGetAssetInformation_thenSucceed() {
        List<String> pairs = List.of("XXBT", "ZUSD", "XETH");
        AssetRequest request = AssetRequest.builder()
            .pairs(pairs)
            .build();
        AssetResult result = CLIENT.getAssetInformation(request);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.getAssets().size());
        result.getAssets().forEach((name, asset) -> {
            Assertions.assertNotNull(asset);
            Assertions.assertTrue(pairs.contains(name) || pairs.contains(asset.getAlternateName()));
        });
    }

    @Test
    public void givenMarketDataClient_whenGetAssetInformationWithInvalidAsset_thenFail() {
        List<String> pairs = List.of("ABC123");
        AssetRequest request = AssetRequest.builder()
            .pairs(pairs)
            .build();
        Assertions.assertThrows(
            FeignException.BadRequest.class,
            () -> CLIENT.getAssetInformation(request),
            "EQuery:Unknown asset"
        );
    }

    @Test
    public void givenMarketDataClient_whenGetTradableAssetPairs_thenSucceed() {
        List<String> pairs = List.of("XXBTZUSD", "XETHXXBT");
        TradableAssetPairRequest request = TradableAssetPairRequest.builder()
            .pairs(pairs)
            .build();
        TradableAssetPairResult result = CLIENT.getTradableAssetPairs(request);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getAssetPairs().size());
        result.getAssetPairs().forEach((pairName, assetPair) -> {
            Assertions.assertTrue(pairs.contains(pairName));
            Assertions.assertNotNull(assetPair);
            Assertions.assertFalse(assetPair.getTakerFees().isEmpty());
        });
    }

    @Test
    public void givenMarketDataClient_whenGetTradableAssetPairsWithInvalidPair_thenFail() {
        List<String> pairs = List.of("ABC/123");
        TradableAssetPairRequest request = TradableAssetPairRequest.builder()
            .pairs(pairs)
            .build();
        Assertions.assertThrows(
            FeignException.BadRequest.class,
            () -> CLIENT.getTradableAssetPairs(request),
            "EQuery:Unknown asset pair"
        );
    }

    @Test
    public void givenMarketDataClient_whenGetOHLCData_thenSucceed() {
        OHLCRequest request = OHLCRequest.builder()
            .pair("XXBTZUSD")
            .build();
        OHLCResult result = CLIENT.getOHLCData(request);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getAssetPairName());
        Assertions.assertFalse(result.getOhlcList().isEmpty());
    }

    @Test
    public void givenMarketDataClient_whenGetOHLCDataWithInvalidPair_thenFail() {
        OHLCRequest request = OHLCRequest.builder()
            .pair("ABC/123")
            .build();
        Assertions.assertThrows(
            FeignException.BadRequest.class,
            () -> CLIENT.getOHLCData(request),
            "EQuery:Unknown asset pair"
        );
    }
}