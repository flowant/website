package org.flowant.website.repository;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.NoRepositoryBean;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface PageableRepository<T, ID> extends IdentityRepository<T, ID> {
    Mono<Slice<T>> findAllByContainerId(UUID containerId, Pageable pageable);
    Flux<T> findAllByContainerId(UUID containerId);
}
