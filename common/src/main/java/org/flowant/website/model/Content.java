package org.flowant.website.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

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
@Table
public class Content implements HasIdCid, HasReputation, HasCruTime, Comparable<Content> {

    @NonNull
    @PrimaryKey
    IdCid idCid;

    @NonNull
    String title; // to be tags always

    Recipe extend; // TODO how to extend gracefully?

    List<FileRef> fileRefs;

    String sentences;

    @Indexed
    Set<String> tags;

    List<UUID> popularReviewIds;

    @NonNull
    Reputation reputation;

    @NonNull
    CRUZonedTime cruTime;

    public int compareTo(Content content) {
        return getIdentity().compareTo(content.getIdentity());
    }

}
