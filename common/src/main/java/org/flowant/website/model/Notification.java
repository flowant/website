package org.flowant.website.model;

import java.util.HashSet;
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

    @NonNull
    @Column("c")
    Category category;

    @Indexed
    @Column("s")
    Set<UUID> subscribers;

    @Column("ri")
    UUID referenceId;

    @Column("rc")
    UUID referenceCid;

    @Column("a")
    String appendix;

    @NonNull
    @Column("t")
    ZonedTime time;

    public Set<UUID> getSubscribers() {
        if (subscribers == null) {
            subscribers = new HashSet<>();
        }
        return subscribers;
    }

    public static Notification fromUser(User user, Category category, Set<UUID> subscribers) {
        return of(IdCid.random(user.getIdentity()), user.getDisplayName(), category, ZonedTime.now())
                .setSubscribers(subscribers);
    }

    public static Notification fromAuthor(HasAuthor<?> author, Category category, Set<UUID> subscribers) {
        return of(IdCid.random(author.getAuthorId()), author.getAuthorName(), category, ZonedTime.now())
                .setSubscribers(subscribers);
    }

}
