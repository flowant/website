package org.flowant.website.model;

import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Element;
import org.springframework.data.cassandra.core.mapping.Tuple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(chain=true)
@AllArgsConstructor
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@Tuple
public class FileRef implements HasIdentity {

    @NonNull
    @Element(0)
    UUID identity; // to be used key in case of using external storage

    @NonNull
    @Element(1)
    String uri;

    @NonNull
    @Element(2)
    String contentType;

    @NonNull
    @Element(3)
    String filename; // client's filename

    @Element(4)
    long length;

    @NonNull
    @Element(5)
    CRUZonedTime cruTime;

}
