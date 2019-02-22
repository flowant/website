package org.flowant.website.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class RepositoryRest <T, ID, R extends ReactiveCassandraRepository<T, ID>> {

    public final static String ID = "id";
    public final static String PATH_SEG_ID = "/{id}";

    @Autowired
    protected R repo;

    public Flux<T> getAll() {
        return repo.findAll();
    }

    public Mono<ResponseEntity<T>> post(T entity) {
        return repo.save(entity)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<T>> put(T entity) {
        return repo.save(entity)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<T>> getById(ID id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<Void>> deleteById(ID id) {
        return repo.deleteById(id)
                .map(ResponseEntity::ok);
    }
}
