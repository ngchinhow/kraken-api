package io.github.ngchinhow.kraken.websocket.model.message.instrument;

import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@lombok.Data
@NoArgsConstructor(force = true)
public class Data {
    @NonNull
    private List<WebSocketsAsset> assets;
    @NonNull
    private List<WebSocketsAssetPair> pairs;
}