package org.flowant.website.rest;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BaseCounterRepositoryRest <Entity, Repository extends ReactiveCrudRepository<Entity, UUID>> {

    public final static String ID = "id";

    @Autowired
    private Repository repo;

    public Flux<Entity> getAll() {
        return repo.findAll();
    }

    public Mono<ResponseEntity<Entity>> post(@Valid @RequestBody Entity entity) {
        return repo.save(entity).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<Entity>> put(@Valid @RequestBody Entity entity) {
        return repo.save(entity).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<Entity>> getById(@PathVariable(value = ID) String id) {
        return repo.findById(UUID.fromString(id)).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id) {
        return repo.deleteById(UUID.fromString(id)).map(ResponseEntity::ok);
    }
}
