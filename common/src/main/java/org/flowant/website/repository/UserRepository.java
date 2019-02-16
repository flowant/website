package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.User;
import org.springframework.data.cassandra.core.cql.QueryOptions;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, UUID> {

    Flux<User> findByUsername(String username);

    Flux<User> findByUsername(String username, QueryOptions opts);

    @AllowFiltering
    Mono<Slice<User>> findByLastname(String lastname, Pageable pageRequest);

    /**
     * Derived query selecting by {@code lastname}.
     *
     * @param lastname
     * @return
     */
    Flux<User> findByLastname(String lastname);

    /**
     * String query selecting one entity.
     *
     * @param lastname
     * @return
     */
    @Query("SELECT * FROM person WHERE firstname = ?0 and lastname  = ?1")
    Mono<User> findByFirstnameInAndLastname(String firstname, String lastname);

    /**
     * Derived query selecting by {@code lastname}. {@code lastname} uses deferred
     * resolution that does not require blocking to obtain the parameter value.
     *
     * @param lastname
     * @return
     */
    Flux<User> findByLastname(Mono<String> lastname);

    /**
     * Derived query selecting by {@code firstname} and {@code lastname}.
     * {@code firstname} uses deferred resolution that does not require blocking to
     * obtain the parameter value.
     *
     * @param firstname
     * @param lastname
     * @return
     */
    Mono<User> findByFirstnameAndLastname(Mono<String> firstname, String lastname);
}
