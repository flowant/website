package org.flowant.website.model;

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
public class Report implements HasIdCid, HasAuthor<Report> {

    //TODO report bug, inquiry, or administrator contact

    @NonNull
    @PrimaryKey
    IdCid idCid;

    @NonNull
    @Indexed
    @Column("ai")
    UUID authorId;

    @NonNull
    @Column("an")
    String authorName;

    @NonNull
    @Column("s")
    String sentences;

    @Column("mr")
    boolean markedAsReviewed = false;

    @NonNull
    @Column("t")
    ZonedTime time;

    public static Report fromUser(UUID reportedId, User reporter, String msg) {
        return of(IdCid.random(reportedId), reporter.getIdentity(), reporter.getDisplayName(), msg, ZonedTime.now());
    }

}
