package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ReplyReputation;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ReplyReputationRepository extends IdentityRepository<ReplyReputation, UUID> {

    static final String UPDATE_COUTERS = "UPDATE replyreputation " +
            "SET liked = liked + ?1, disliked = disliked + ?2, reported = reported + ?3, " +
            "reputed = reputed + ?4 WHERE identity = ?0";

    @Query(UPDATE_COUTERS)
    Mono<Object> accumulate(UUID id, long liked, long disliked, long reported,long reputed);

    default Mono<Void> accumulate(ReplyReputation rr) {
        return accumulate(rr.getIdentity(), rr.getLiked(), rr.getDisliked(),
                rr.getReported(), rr.getReputed()).then();
    };

    default Mono<ReplyReputation> save(ReplyReputation rr) {
        return accumulate(rr).then(findById(rr.getIdentity()));
    };
}
