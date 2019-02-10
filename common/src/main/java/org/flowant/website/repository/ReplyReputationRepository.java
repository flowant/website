package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ReplyReputation;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

public interface ReplyReputationRepository extends ReactiveCrudRepository<ReplyReputation, UUID> {

    static final String UPDATE_COUTERS = "UPDATE replyreputation " +
            "SET liked = liked + ?1, disliked = disliked + ?2, reported = reported + ?3, " +
            "reputed = reputed + ?4 WHERE id = ?0";

    @Query(UPDATE_COUTERS)
    Mono<ReplyReputation> accumulate(UUID id, long liked, long disliked, long reported,long reputed);

    default Mono<ReplyReputation> accumulate(ReplyReputation rr) {
        return accumulate(rr.getId(), rr.getLiked(), rr.getDisliked(),
                rr.getReported(), rr.getReputed());
    };

    default Mono<ReplyReputation> save(ReplyReputation rr) {
        return accumulate(rr);
    };
}
