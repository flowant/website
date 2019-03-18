package org.flowant.website.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@AllArgsConstructor(staticName="of")
@NoArgsConstructor
@Table
public class Relation {

    @NonNull
    @PrimaryKey("id")
    UUID identity;

    @NonNull
    @Column("f")
    Set<UUID> followings;

    @NonNull
    @Column("fr")
    Set<UUID> followers;

    public Set<UUID> getFollowings() {
        if (followings == null) {
            followings = new HashSet<>();
        }
        return followings;
    }

    public Set<UUID> getFollowers() {
        if (followers == null) {
            followers = new HashSet<>();
        }
        return followers;
    }

}
