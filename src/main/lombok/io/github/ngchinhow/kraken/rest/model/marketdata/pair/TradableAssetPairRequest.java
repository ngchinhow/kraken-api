package io.github.ngchinhow.kraken.rest.model.marketdata.pair;

import com.fasterxml.jackson.annotation.JsonValue;
import feign.Param;
import io.github.ngchinhow.kraken.rest.model.AbstractRequest;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class TradableAssetPairRequest implements AbstractRequest {
    @Param("pair")
    private List<String> pairs;
    private Info info;

    @Getter(onMethod = @__(@JsonValue))
    @RequiredArgsConstructor
    public enum Info {
        INFO("info"),
        LEVERAGE("leverage"),
        FEES("fees"),
        MARGIN("margin");

        private final String info;
    }
}