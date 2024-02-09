package io.github.ngchinhow.kraken.rest.model;

import lombok.Data;

import java.util.List;

@Data
public class KrakenResponse<T extends AbstractResult> {
    private List<String> error;
    private T result;
}
