package org.flowant.website.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

@NoRepositoryBean
public interface IdentityRepository<T, ID> extends ReactiveCrudRepository<T, ID> {
}
