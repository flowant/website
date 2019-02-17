package org.flowant.website.rest;

import java.util.UUID;

import javax.validation.Valid;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.HasCruTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BaseRepositoryRest <Entity extends HasCruTime, Repository extends ReactiveCrudRepository<Entity, UUID>> {

    public final static String ID = "id";

    @Autowired
    protected Repository repo;

    public Flux<Entity> getAll() {
        return repo.findAll().doOnNext(entity -> entity.getCruTime().readNow());
    }

    public Mono<ResponseEntity<Entity>> post(@Valid @RequestBody Entity entity) {
        entity.setCruTime(CRUZonedTime.now());
        return repo.save(entity).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<Entity>> put(@Valid @RequestBody Entity entity) {
        entity.getCruTime().updatedNow();
        return repo.save(entity).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<Entity>> getById(@PathVariable(value = ID) String id) {
        return repo.findById(UUID.fromString(id)).doOnNext(entity -> entity.getCruTime().readNow())
                .map(ResponseEntity::ok).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id) {
        return repo.deleteById(UUID.fromString(id))
                .map(ResponseEntity::ok);
    }
}
