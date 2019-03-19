package org.flowant.website.model;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
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
    @PrimaryKey
    IdCid idCid;

    @CassandraType(type=Name.COUNTER)
    @Column("v")
    long viewed;

    @CassandraType(type=Name.COUNTER)
    @Column("r")
    long rated;

    @CassandraType(type=Name.COUNTER)
    @Column("l")
    long liked;

    @CassandraType(type=Name.COUNTER)
    @Column("d")
    long disliked;

    @CassandraType(type=Name.COUNTER)
    @Column("rt")
    long reported;

    @CassandraType(type=Name.COUNTER)
    @Column("rp")
    long reputed;

    public static ContentReputation of(IdCid id, Reputation r) {
        return of(id, r.getViewed(), r.getRated(), r.getLiked(), r.getDisliked(), r.getReported(), r.getReputed());
    }

}
