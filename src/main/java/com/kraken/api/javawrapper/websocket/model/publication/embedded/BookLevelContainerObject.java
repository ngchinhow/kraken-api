package com.kraken.api.javawrapper.websocket.model.publication.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookLevelContainerObject {
    private List<BookLevelEmbeddedObject> levels;
    private Integer checksum;
}
