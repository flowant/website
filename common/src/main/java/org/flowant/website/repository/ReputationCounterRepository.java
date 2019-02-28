package org.flowant.website.repository;

import org.flowant.website.model.ReputationCounter;
import org.springframework.data.repository.NoRepositoryBean;

import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface ReputationCounterRepository<T extends ReputationCounter> extends IdCidRepository<T> {

    default Mono<Void> deleteTestdataWithRelationship(T entity) {
        return delete(entity)
                .then(RelationshipService.deleteReputation(entity))
                .then(RelationshipService.deleteSubItemById(entity.getContainerId()));
    }

}
