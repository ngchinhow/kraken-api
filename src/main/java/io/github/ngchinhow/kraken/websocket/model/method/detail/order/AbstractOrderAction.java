package io.github.ngchinhow.kraken.websocket.model.method.detail.order;


import io.github.ngchinhow.kraken.websocket.model.method.detail.AbstractParameter;
import io.github.ngchinhow.kraken.websocket.model.method.detail.AbstractResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public abstract class AbstractOrderAction {

    @Data
    @EqualsAndHashCode
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static abstract class Parameter implements AbstractParameter {
        private String token;
    }

    @Data
    @NoArgsConstructor
    public static abstract class Result implements AbstractResult {

    }
}
