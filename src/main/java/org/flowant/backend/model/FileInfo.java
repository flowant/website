package org.flowant.backend.model;

import java.util.UUID;

public interface FileInfo {
    UUID getId();
    UUID getContentId();
    String getContentType();
    String getFilename();
    long getLength();
    CRUDZonedTime getCrudTime();
}
