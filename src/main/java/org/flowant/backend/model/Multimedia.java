package org.flowant.backend.model;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@Table
public class Multimedia {
    @Id
    @NonNull
    UUID id;
    @NonNull
    UUID contentId; // owner of this media;
    @NonNull
    ByteBuffer media; //TODO to be chunked and parallel
    @NonNull
    String contentType;
    @NonNull
    String originalFilename; // client's filename
    long length; //TODO should be filled before being inserted to DB
    @NonNull
    CRUDZonedTime crudTime;
}
