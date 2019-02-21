package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.HasMapId;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface MapIdRepository<T extends HasMapId> extends ReactiveCrudRepository<T, MapId> {

    default Mono<Void> deleteWithParent(T entity) {
        return delete(entity).then(RelationshipService.deleteParent(entity));
    }

    Flux<T> findAllByContainerId(UUID containerId);

    Mono<Object> deleteAllByContainerId(UUID containerId);
}
