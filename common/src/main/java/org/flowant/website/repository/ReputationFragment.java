package org.flowant.website.repository;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.Reputation;

import reactor.core.publisher.Mono;


public interface ReputationFragment<T> {

    Mono<Reputation> updateReputationById(IdCid idCid, Reputation reputation, Class<T> entityClass);

}
