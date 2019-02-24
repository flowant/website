package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.Reputation;
import org.flowant.website.model.Review;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ReviewRepository extends ReputationRepository<Review>, ReputationFragment<Review> {

    @Override
    default Mono<Reputation> updateReputationById(IdCid idCid, Reputation reputation) {
        return updateReputationById(idCid, reputation, Review.class)
                .thenReturn(reputation);
    }

    @Override
    @Query("delete from review where containerid = ?0")
    Mono<Object> deleteAllByIdCidContainerId(UUID containerId);

}
