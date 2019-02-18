package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ContentReputation;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ContentReputationRepository extends IdentityRepository<ContentReputation, UUID> {

    static final String UPDATE_COUTERS = "UPDATE contentreputation " +
            "SET viewed = viewed + ?1, rated = rated + ?2, liked = liked + ?3, " +
            "disliked = disliked + ?4, reported = reported + ?5, reputed = reputed + ?6 " +
            "WHERE identity = ?0";

    @Query(UPDATE_COUTERS)
    Mono<Object> accumulate(UUID id, long viewed, long rated,
            long liked, long disliked, long reported,long reputed);

    default Mono<Void> accumulate(ContentReputation cr) {
        return accumulate(cr.getIdentity(), cr.getViewed(), cr.getRated(),
                cr.getLiked(), cr.getDisliked(), cr.getReported(), cr.getReputed()).then();
    };

    default Mono<ContentReputation> save(ContentReputation cr) {
        return accumulate(cr).then(findById(cr.getIdentity()));
    };
}
