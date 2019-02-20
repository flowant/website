package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ReviewReputation;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ReviewReputationRepository extends MapIdRepository<ReviewReputation> {

    static final String ACCUMULATE = "UPDATE reviewreputation " +
            "SET viewed = viewed + ?2, rated = rated + ?3, liked = liked + ?4, " +
            "disliked = disliked + ?5, reported = reported + ?6, reputed = reputed + ?7 " +
            "WHERE identity = ?0 and containerid = ?1";

    @Query(ACCUMULATE)
    Mono<Object> accumulate(UUID id, UUID containerId, long viewed, long rated,
            long liked, long disliked, long reported,long reputed);

    default Mono<Void> accumulate(ReviewReputation rr) {
        return accumulate(rr.getIdentity(), rr.getContainerId(), rr.getViewed(), rr.getRated(),
                rr.getLiked(), rr.getDisliked(), rr.getReported(), rr.getReputed()).then();
    };

    @Override
    default Mono<ReviewReputation> save(ReviewReputation rr) {
        return accumulate(rr).then(findById(rr.getMapId()).flatMap(RelationshipService::updateReputation));
    };

    @Override
    @Query("delete from reviewreputation where containerid = ?0")
    Mono<Object> deleteAllByContainerId(UUID containerId);

    @Query("SELECT WRITETIME (reputed) from reviewreputation WHERE id = ?0")
    Mono<Long> writetimeMicros(UUID id);

}
