package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.HasMapId;
import org.flowant.website.model.HasReputation;
import org.flowant.website.model.Reputation;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.NoRepositoryBean;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface PageableRepository<T extends HasMapId & HasReputation, ID> extends MapIdRepository<T, ID> {

    Mono<Slice<T>> findAllByContainerId(UUID containerId, Pageable pageable);

    Flux<T> findAllByContainerId(UUID containerId);

    Mono<Reputation> updateReputationById(MapId id, Reputation reputation);

}
