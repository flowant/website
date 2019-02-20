package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ContentReputation;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ContentReputationRepository extends MapIdRepository<ContentReputation, MapId> {

    static final String ACCUMULATE = "UPDATE contentreputation " +
            "SET viewed = viewed + ?2, rated = rated + ?3, liked = liked + ?4, " +
            "disliked = disliked + ?5, reported = reported + ?6, reputed = reputed + ?7 " +
            "WHERE identity = ?0 and containerid = ?1";

    @Query(ACCUMULATE)
    Mono<Object> accumulate(UUID id, UUID containerId, long viewed, long rated,
            long liked, long disliked, long reported,long reputed);

    default Mono<Void> accumulate(ContentReputation cr) {
        return accumulate(cr.getIdentity(), cr.getContainerId(), cr.getViewed(), cr.getRated(),
                cr.getLiked(), cr.getDisliked(), cr.getReported(), cr.getReputed()).then();
    };

    default Mono<ContentReputation> save(ContentReputation cr) {
        return accumulate(cr).then(findById(cr.getMapId()).flatMap(RelationshipService::updateReputation));
    };
}
