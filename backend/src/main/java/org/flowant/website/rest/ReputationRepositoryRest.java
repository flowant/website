package org.flowant.website.rest;

import java.util.UUID;

import org.flowant.website.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class ReputationRepositoryRest <Entity, Repository extends IdentityRepository<Entity, UUID>> {

    public final static String ID = "id";
    public final static String PATH_SEG_ID = "/{id}";

    @Autowired
    private Repository repo;

    public Flux<Entity> getAll() {
        return repo.findAll();
    }

    public Mono<ResponseEntity<Entity>> post(Entity entity) {
        return repo.save(entity).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<Entity>> put(Entity entity) {
        return repo.save(entity).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<Entity>> getById(String id) {
        return repo.findById(UUID.fromString(id)).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<Void>> deleteById(String id) {
        return repo.deleteById(UUID.fromString(id)).map(ResponseEntity::ok);
    }
}
