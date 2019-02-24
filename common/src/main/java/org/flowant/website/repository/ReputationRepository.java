package org.flowant.website.repository;

import org.flowant.website.model.HasIdCid;
import org.flowant.website.model.HasReputation;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.Reputation;
import org.springframework.data.repository.NoRepositoryBean;

import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface ReputationRepository<T extends HasIdCid & HasReputation>
        extends IdCidRepository<T> {

    Mono<Reputation> updateReputationById(IdCid idCid, Reputation reputation);

}
