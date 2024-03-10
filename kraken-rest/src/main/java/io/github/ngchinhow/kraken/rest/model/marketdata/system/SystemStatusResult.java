package io.github.ngchinhow.kraken.rest.model.marketdata.system;

import io.github.ngchinhow.kraken.common.enumerations.Status;
import io.github.ngchinhow.kraken.rest.model.ResultInterface;

import java.time.ZonedDateTime;

public record SystemStatusResult(Status status, ZonedDateTime timestamp) implements ResultInterface {
}
