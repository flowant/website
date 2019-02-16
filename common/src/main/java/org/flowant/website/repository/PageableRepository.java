package org.flowant.website.repository;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface PageableRepository<T, ID> extends ReactiveCrudRepository<T, ID> {
    Mono<Slice<T>> findAllByContainerId(UUID containerId, Pageable pageable);
}
