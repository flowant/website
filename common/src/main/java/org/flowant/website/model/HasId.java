package org.flowant.website.model;

import java.util.UUID;

public interface HasId {
    UUID getId();
    HasId setId(UUID id);
}
