package io.github.ngchinhow.kraken.websockets.model.method;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractResult {
    private List<String> warnings;
}
