package org.flowant.backend.model;

import java.util.UUID;

public interface MultimediaInfo {
    UUID getId();
    UUID getContentId();
    String getContentType();
    String getOriginalFilename();
    long getLength();
    CRUDZonedTime getCrudTime();
}
