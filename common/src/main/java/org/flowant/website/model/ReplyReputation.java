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
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(chain=true)
@AllArgsConstructor(staticName="of")
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@Table
public class ReplyReputation implements HasIdentity {
    @Id @NonNull
    UUID identity;
    @CassandraType(type=Name.COUNTER)
    long liked;
    @CassandraType(type=Name.COUNTER)
    long disliked;
    @CassandraType(type=Name.COUNTER)
    long reported;
    @CassandraType(type=Name.COUNTER)
    long reputed;
}
