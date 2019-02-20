package org.flowant.website.model;

import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType.Name;

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
@AllArgsConstructor(staticName="of")
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@Table
public class ContentReputation implements ReputationCounter {
    @NonNull
    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    UUID identity;
    @NonNull
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    UUID containerId;
    @CassandraType(type=Name.COUNTER)
    long viewed;
    @CassandraType(type=Name.COUNTER)
    long rated;
    @CassandraType(type=Name.COUNTER)
    long liked;
    @CassandraType(type=Name.COUNTER)
    long disliked;
    @CassandraType(type=Name.COUNTER)
    long reported;
    @CassandraType(type=Name.COUNTER)
    long reputed;

    public static ContentReputation of(MapId id, Reputation r) {
        return of((UUID) id.get(IDENTITY), (UUID) id.get(CONTAINER_ID),
                r.getViewed(), r.getRated(), r.getLiked(),
                r.getDisliked(), r.getReported(), r.getReputed());
    }
}
