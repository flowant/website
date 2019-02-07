package org.flowant.website.model;

import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@UserDefinedType
public class FileRef {
    @NonNull
    UUID id; // to be used key in case of using external storage
    @NonNull
    String uri; //TODO can be external links or internal storage url
    @NonNull
    String contentType;
    @NonNull
    String filename; // client's filename
    long length; //TODO should be filled before being inserted to DB
    @NonNull
    CRUZonedTime cruTime;

    public FileRef setLength(long length) {
        this.length = length;
        return this;
    }
}
