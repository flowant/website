package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ReplyReputation;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ReplyReputationRepository extends MapIdRepository<ReplyReputation, MapId> {

    static final String ACCUMULATE = "UPDATE replyreputation " +
            "SET viewed = viewed + ?2, rated = rated + ?3, liked = liked + ?4, " +
            "disliked = disliked + ?5, reported = reported + ?6, reputed = reputed + ?7 " +
            "WHERE identity = ?0 and containerid = ?1";

    @Query(ACCUMULATE)
    Mono<Object> accumulate(UUID id, UUID containerId, long viewed, long rated,
            long liked, long disliked, long reported,long reputed);

    default Mono<Void> accumulate(ReplyReputation rr) {
        return accumulate(rr.getIdentity(), rr.getContainerId(), rr.getViewed(), rr.getRated(),
                rr.getLiked(), rr.getDisliked(), rr.getReported(), rr.getReputed()).then();
    };

    default Mono<ReplyReputation> save(ReplyReputation rr) {
        return accumulate(rr).then(findById(rr.getMapId()).flatMap(RelationshipService::updateReputation));
    };

}
