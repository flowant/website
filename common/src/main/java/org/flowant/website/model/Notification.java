package org.flowant.website.model;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@AllArgsConstructor
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@Table
public class Notification implements HasIdCid {

    @NonNull
    @PrimaryKey
    @Column("ic")
    // containerId means authorId; source of notification
    IdCid idCid;

    @NonNull
    @Column("an")
    String authorName;

    @Indexed
    @NonNull
    @Column("s")
    Set<UUID> subscribers;

    @NonNull
    @Column("c")
    Category category;

    @Column("ri")
    UUID referenceId;

    @Column("rc")
    UUID referenceCid;

    @Column("a")
    String appendix;

}
