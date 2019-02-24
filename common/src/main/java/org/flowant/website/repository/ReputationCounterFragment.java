package org.flowant.website.repository;

import org.flowant.website.model.ReputationCounter;

import reactor.core.publisher.Mono;


public interface ReputationCounterFragment<T extends ReputationCounter> {

    Mono<Boolean> accumulate(T entity, Class<T> entityClass);

}
