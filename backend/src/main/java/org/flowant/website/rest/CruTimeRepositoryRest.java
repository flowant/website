package org.flowant.website.rest;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.HasCruTime;
import org.flowant.website.model.HasMapId;
import org.flowant.website.repository.MapIdRepository;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class CruTimeRepositoryRest <T extends HasMapId & HasCruTime, R extends MapIdRepository<T>>
        extends MapIdRepositoryRest<T, R> {

    public Flux<T> getAll() {
        return repo.findAll()
                .doOnNext(entity -> entity.getCruTime().readNow());
    }

    public Mono<ResponseEntity<T>> post(T entity) {
        entity.setCruTime(CRUZonedTime.now());
        return repo.save(entity)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<T>> put(T entity) {
        entity.getCruTime().updatedNow();
        return repo.save(entity)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<T>> getById(MapId id) {
        return repo.findById(id)
                .doOnNext(entity -> entity.getCruTime().readNow())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
