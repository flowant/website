package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ReviewReputation;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ReviewReputationRepository extends IdentityRepository<ReviewReputation, UUID> {

    static final String UPDATE_COUTERS = "UPDATE reviewreputation " +
            "SET liked = liked + ?1, disliked = disliked + ?2, reported = reported + ?3, " +
            "reputed = reputed + ?4 WHERE identity = ?0";

    @Query(UPDATE_COUTERS)
    Mono<Object> accumulate(UUID id, long liked, long disliked, long reported,long reputed);

    default Mono<Void> accumulate(ReviewReputation rr) {
        return accumulate(rr.getIdentity(), rr.getLiked(), rr.getDisliked(),
                rr.getReported(), rr.getReputed()).then();
    };

    @Override
    default Mono<ReviewReputation> save(ReviewReputation rr) {
        return accumulate(rr).then(findById(rr.getIdentity()));
    };

    @Query("SELECT WRITETIME (reputed) from reviewreputation WHERE id = ?0")
    Mono<Long> writetimeMicros(UUID id);
}
