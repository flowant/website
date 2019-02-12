package org.flowant.website.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType.Name;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@AllArgsConstructor(staticName="of")
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@Table
public class ContentReputation {
    @Id @NonNull
    UUID id;
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
}
