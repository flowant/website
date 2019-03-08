package org.flowant.website.repository;

import static org.springframework.data.cassandra.core.query.Criteria.where;

import java.util.UUID;

import org.flowant.website.model.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.cassandra.core.query.Update;

import reactor.core.publisher.Mono;

public class RelationFragmentImpl implements RelationFragment {

    @Autowired
    private ReactiveCassandraOperations operations;

    @Override
    public Mono<UUID> follow(UUID identity, UUID followee) {
        return addFollowing(identity, followee)
                .then(addFollower(followee, identity))
                .thenReturn(followee);
    }

    @Override
    public Mono<UUID> unfollow(UUID identity, UUID followee) {
        return removeFollowing(identity, followee)
                .then(removeFollower(followee, identity))
                .thenReturn(followee);
    }

    public Mono<UUID> addFollowing(UUID identity, UUID followee) {

        return operations.update(
                Query.query(where("identity").is(identity)),
                Update.empty().addTo("followings").append(followee),
                Relation.class)
                .thenReturn(followee);
    }

    public Mono<UUID> addFollower(UUID identity, UUID follower) {

        return operations.update(
                Query.query(where("identity").is(identity)),
                Update.empty().addTo("followers").append(follower),
                Relation.class)
                .thenReturn(follower);
    }

    public Mono<UUID> removeFollowing(UUID identity, UUID followee) {

        return operations.update(
                Query.query(where("identity").is(identity)),
                Update.empty().remove("followings", followee),
                Relation.class)
                .thenReturn(followee);
    }

    public Mono<UUID> removeFollower(UUID identity, UUID follower) {

        return operations.update(
                Query.query(where("identity").is(identity)),
                Update.empty().remove("followers", follower),
                Relation.class)
                .thenReturn(follower);
    }

}
