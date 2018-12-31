package org.flowant.users.data;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;


public interface ReplyRepository extends ReactiveCrudRepository<Reply, UUID> {

	/**
	 * Derived query selecting by {@code id}.
	 *
	 * @param lastname
	 * @return
	 */
	Mono<Reply> findById(UUID id);

}
