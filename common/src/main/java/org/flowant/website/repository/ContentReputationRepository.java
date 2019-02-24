package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ContentReputation;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ContentReputationRepository extends ReputationCounterRepository<ContentReputation>,
                                                     ReputationCounterFragment<ContentReputation> {

    @Override
    default Mono<ContentReputation> save(ContentReputation cr) {
        return accumulate(cr, ContentReputation.class)
                .then(findById(cr.getIdCid()).flatMap(RelationshipService::updateReputation));
    };

    @Override
    @Query("delete from contentreputation where containerid = ?0")
    Mono<Object> deleteAllByIdCidContainerId(UUID containerId);

}
