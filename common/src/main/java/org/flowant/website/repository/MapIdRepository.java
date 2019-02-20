package org.flowant.website.repository;

import org.flowant.website.model.HasMapId;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface MapIdRepository<T extends HasMapId, ID> extends ReactiveCrudRepository<T, ID> {

    default Mono<Void> deleteWithParent(T entity) {
        return delete(entity).then(RelationshipService.deleteParent(entity));
    }

}
