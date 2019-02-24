package org.flowant.website.model;

import java.util.List;
import java.util.UUID;

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
public class Review implements HasIdCid, HasReputation, HasCruTime {

    @NonNull
    @PrimaryKey
    IdCid idCid;

    @NonNull
    UUID reviewerId;

    @NonNull
    String reviewerName;

    @NonNull
    Reputation reputing; // reputing content

    String comment;

    List<UUID> popularReplyIds;

    @NonNull
    Reputation reputation;

    @NonNull
    CRUZonedTime cruTime;

}
