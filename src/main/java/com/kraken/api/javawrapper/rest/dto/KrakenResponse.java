package com.kraken.api.javawrapper.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class KrakenResponse<T> {
    private List<String> error;
    private T result;
}
