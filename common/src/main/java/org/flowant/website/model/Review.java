package org.flowant.website.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Indexed;
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
public class Review implements HasId, HasCruTime {
    @NonNull @Id
    UUID id;
    @NonNull @Indexed
    UUID containerId;
    @NonNull
    UUID reviewerId;
    @NonNull
    String reviewerName;
    @NonNull
    Reputing reputing;
    String comment;
    List<UUID> popularReplyIds;
    @NonNull
    CRUZonedTime cruTime;
}
