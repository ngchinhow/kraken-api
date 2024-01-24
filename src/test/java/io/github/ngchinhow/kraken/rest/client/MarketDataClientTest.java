package io.github.ngchinhow.kraken.rest.client;

import feign.FeignException;
import io.github.ngchinhow.kraken.rest.model.marketdata.asset.AssetRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.asset.AssetResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.asset.RestAsset;
import io.github.ngchinhow.kraken.rest.model.marketdata.ohlc.OHLCRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.ohlc.OHLCResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.pair.RestAssetPair;
import io.github.ngchinhow.kraken.rest.model.marketdata.pair.TradableAssetPairRequest;
import io.github.ngchinhow.kraken.rest.model.marketdata.pair.TradableAssetPairResult;
import io.github.ngchinhow.kraken.rest.model.marketdata.servertime.ServerTimeResult;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarketDataClientTest extends PublicClientTest {
    private static MarketDataClient CLIENT;

    @BeforeAll
    static void beforeAll() {
        CLIENT = KRAKEN_CONNECTION_MANAGER.getRestClient(MarketDataClient.class);
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
    void givenMarketDataClient_whenGetAssetInformation_thenSucceed() {
        List<String> pairs = List.of("XXBT", "ZUSD", "XETH");
        AssetRequest request = AssetRequest.builder()
                                           .pairs(pairs)
                                           .build();
        AssetResult result = CLIENT.getAssetInformation(request);
        assertThat(result)
            .isNotNull()
            .extracting(AssetResult::getAssets, InstanceOfAssertFactories.map(String.class, RestAsset.class))
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
                        InstanceOfAssertFactories.map(String.class, RestAssetPair.class))
            .hasSize(2)
            .allSatisfy((pairName, assetPair) -> {
                assertThat(pairName)
                    .isIn(pairs);
                assertThat(assetPair)
                    .isNotNull()
                    .extracting(RestAssetPair::getTakerFees, InstanceOfAssertFactories.LIST)
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
}
