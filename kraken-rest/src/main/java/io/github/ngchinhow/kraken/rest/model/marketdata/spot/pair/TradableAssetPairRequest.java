package io.github.ngchinhow.kraken.rest.model.marketdata.spot.pair;

import feign.Param;
import io.github.ngchinhow.kraken.common.enumerations.BaseKrakenEnum;
import io.github.ngchinhow.kraken.rest.model.RequestInterface;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public final class TradableAssetPairRequest implements RequestInterface {
    @Param("pair")
    private List<String> pairs;
    private Info info;

    public enum Info implements BaseKrakenEnum {
        INFO,
        LEVERAGE,
        FEES,
        MARGIN
    }
}