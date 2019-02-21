package org.flowant.website.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
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
public class Content implements HasMapId, HasReputation, HasCruTime {
    @NonNull
    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    UUID identity;
    @NonNull
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    UUID containerId;
    @NonNull
    String title; // to be tags always
    Recipe extend; // TODO how to extend gracefully?
    List<FileRef> fileRefs;
    String sentences;
    @Indexed
    Set<String> tags;
    List<UUID> popularReviewIds;
    Reputation reputation;
    @NonNull
    CRUZonedTime cruTime;
}
