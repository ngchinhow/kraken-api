package com.kraken.api.javawrapper.websocket.model.event.response;

import com.kraken.api.javawrapper.websocket.dto.RequestIdentifier;

public interface IResponseMessage {

    RequestIdentifier toRequestIdentifier();
}
