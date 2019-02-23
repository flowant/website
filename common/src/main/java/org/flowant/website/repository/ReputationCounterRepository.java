package org.flowant.website.repository;

import org.flowant.website.model.ReputationCounter;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ReputationCounterRepository<T extends ReputationCounter> extends IdCidRepository<T> {

}
