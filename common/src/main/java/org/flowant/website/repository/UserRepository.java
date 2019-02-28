package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.User;
import org.springframework.data.cassandra.core.cql.QueryOptions;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCassandraRepository<User, UUID> {

    Flux<User> findByUsername(String username);

    Flux<User> findByUsername(String username, QueryOptions opts);

    // for test
    @AllowFiltering
    Mono<Slice<User>> findByLastname(String lastname, Pageable pageRequest);

}
