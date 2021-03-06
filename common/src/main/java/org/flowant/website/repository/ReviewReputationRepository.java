package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ReviewReputation;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ReviewReputationRepository extends ReputationCounterRepository<ReviewReputation>,
                                                    ReputationCounterFragment<ReviewReputation> {

    @Override
    default Mono<ReviewReputation> save(ReviewReputation rr) {
        return accumulate(rr)
                .then(findById(rr.getIdCid())
                        .flatMap(RelationshipService::updateReputation)
                        .flatMap(RelationshipService::updatePopularSubItems));
    };

    @Override
    @Query("delete from reviewreputation where cid = ?0")
    Mono<Object> deleteAllByIdCidContainerId(UUID containerId);

}
