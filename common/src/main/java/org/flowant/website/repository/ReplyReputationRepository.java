package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ReplyReputation;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ReplyReputationRepository extends ReputationCounterRepository<ReplyReputation>,
                                                   ReputationCounterFragment<ReplyReputation> {

    @Override
    default Mono<ReplyReputation> save(ReplyReputation rr) {
        return accumulate(rr, ReplyReputation.class)
                .then(findById(rr.getIdCid())
                        .flatMap(RelationshipService::updateReputation)
                        .flatMap(RelationshipService::updatePopularSubItems));
    };

    @Override
    @Query("delete from replyreputation where containerid = ?0")
    Mono<Object> deleteAllByIdCidContainerId(UUID containerId);

}
