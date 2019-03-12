package org.flowant.website.model;

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
public class Reply implements HasIdCid, HasAuthor<Reply>, HasReputation, HasCruTime {

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

    @Column("c")
    String comment;

    @NonNull
    @Column("rp")
    Reputation reputation;

    @NonNull
    @Column("ct")
    CRUZonedTime cruTime;

}
