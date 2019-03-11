package org.flowant.website.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Column;
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
    @Column("ic")
    IdCid idCid;

    @NonNull
    @Indexed
    @Column("ai")
    UUID authorId;

    @NonNull
    @Column("an")
    String authorName;

    @NonNull
    @Column("t")
    String title; // to be tags always

    @Column("r")
    Recipe extend; // TODO how to extend gracefully?

    @Column("fr")
    List<FileRef> fileRefs;

    @Column("s")
    String sentences;

    @Indexed
    @Column("tg")
    Set<String> tags;

    @NonNull
    @Column("rp")
    Reputation reputation;

    @NonNull
    @Column("ct")
    CRUZonedTime cruTime;

    public int compareTo(Content content) {
        return getIdentity().compareTo(content.getIdentity());
    }

}
