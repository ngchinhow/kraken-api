package org.trading.krakenapi.rest.client;

import org.springframework.web.bind.annotation.GetMapping;

public interface MarketDataClient extends RestClient {
    @GetMapping("/0/public/AssetPairs")
    void getTradeableAssetPairs();
}
