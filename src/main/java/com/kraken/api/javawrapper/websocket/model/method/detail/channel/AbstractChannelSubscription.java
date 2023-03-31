package com.kraken.api.javawrapper.websocket.model.method.detail.channel;

import com.kraken.api.javawrapper.websocket.model.method.detail.AbstractParameter;
import com.kraken.api.javawrapper.websocket.model.method.detail.AbstractResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public abstract class AbstractChannelSubscription {
    @Data
    @EqualsAndHashCode
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static abstract class Parameter implements AbstractParameter {
        private String channel;
        private Boolean snapshot;
    }

    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    public static abstract class Result implements AbstractResult {
        private String channel;
        private Boolean snapshot;
    }
}
