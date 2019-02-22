package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.HasIdCid;
import org.flowant.website.model.IdCid;
import org.reactivestreams.Publisher;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.NoRepositoryBean;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface IdCidRepository<T extends HasIdCid> extends ReactiveCassandraRepository<T, IdCid> {

    Mono<Slice<T>> findAllByIdCidContainerId(UUID containerId, Pageable pageable);

    Flux<T> findAllByIdCidContainerId(UUID containerId);

    Mono<Object> deleteAllByIdCidContainerId(UUID containerId);

    default Mono<Void> deleteWithParent(T entity) {
        return delete(entity).then(RelationshipService.deleteParent(entity));
    }

}
