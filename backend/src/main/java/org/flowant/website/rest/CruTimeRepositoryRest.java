package org.flowant.website.rest;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.HasCruTime;
import org.flowant.website.model.HasIdCid;
import org.flowant.website.model.IdCid;
import org.flowant.website.repository.IdCidRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class CruTimeRepositoryRest <T extends HasIdCid & HasCruTime, R extends IdCidRepository<T>>
        extends IdCidRepositoryRest<T, R> {

    public Flux<T> getAll() {
        return super.getAll()
                .doOnNext(entity -> entity.getCruTime().readNow());
    }

    public Mono<ResponseEntity<T>> post(T entity) {
        entity.setCruTime(CRUZonedTime.now());
        return super.post(entity);
    }

    public Mono<ResponseEntity<T>> put(T entity) {
        entity.getCruTime().updatedNow();
        return super.put(entity);
    }

    public Mono<ResponseEntity<T>> getById(IdCid idCid) {
        return repo.findById(idCid)
                .doOnNext(entity -> entity.getCruTime().readNow())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
