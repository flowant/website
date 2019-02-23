package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ReplyReputation;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ReplyReputationRepository extends IdCidRepository<ReplyReputation> {

    String ACCUMULATE = "UPDATE replyreputation " +
            "SET viewed = viewed + ?2, rated = rated + ?3, liked = liked + ?4, " +
            "disliked = disliked + ?5, reported = reported + ?6, reputed = reputed + ?7 " +
            "WHERE identity = ?0 and containerid = ?1";

    @Query(ACCUMULATE)
    Mono<Object> accumulate(UUID identity, UUID containerId, long viewed, long rated,
            long liked, long disliked, long reported,long reputed);

    default Mono<Void> accumulate(ReplyReputation rr) {
        return accumulate(rr.getIdentity(), rr.getContainerId(), rr.getViewed(), rr.getRated(),
                rr.getLiked(), rr.getDisliked(), rr.getReported(), rr.getReputed()).then();
    };

    default Mono<ReplyReputation> save(ReplyReputation rr) {
        return accumulate(rr).then(findById(rr.getIdCid()).flatMap(RelationshipService::updateReputation));
    };

    @Override
    @Query("delete from replyreputation where containerid = ?0")
    Mono<Object> deleteAllByIdCidContainerId(UUID containerId);

}
