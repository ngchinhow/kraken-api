package io.github.ngchinhow.kraken.rest.model.marketdata.system;

import io.github.ngchinhow.kraken.common.enumerations.Status;
import io.github.ngchinhow.kraken.rest.model.AbstractResult;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public final class SystemStatusResult implements AbstractResult {
    private Status status;
    private ZonedDateTime timestamp;
}
