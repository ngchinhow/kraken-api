package io.github.ngchinhow.kraken.rest.model.marketdata.asset;

import feign.Param;
import io.github.ngchinhow.kraken.rest.model.RequestInterface;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public final class AssetRequest implements RequestInterface {
    @Param("asset")
    private List<String> pairs;
    @Param("aclass")
    private String assetClass;
}